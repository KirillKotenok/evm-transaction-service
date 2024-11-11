package ua.kornello.evm_transaction_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;
import ua.kornello.evm_transaction_service.mapper.BlockTransactionMapper;
import ua.kornello.evm_transaction_service.repo.SyncStateRepository;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProducerService {

    private final SyncStateRepository syncStateRepository;
    private final BlockTransactionMapper transactionMapper;
    private final KafkaTemplate<String, BlockTransactionDto> kafkaTemplate;
    private static final String KAFKA_TOPIC = "transactions";

    /**
     * Method that process block, push it`s transactions one by one to kafka and save number as the last processed block
     * to db for continue processing from this point if smth happen.
     *
     * @param block ethereum block
     */
    @Transactional
    @Retryable(retryFor = IOException.class, backoff = @Backoff(delay = 5000))
    public void publishTransaction(EthBlock block) {
        log.info("Received block: {}", block.getBlock().getHash());

        block.getBlock().getTransactions().forEach(tx -> {
            Transaction transaction = (Transaction) tx.get();
            kafkaTemplate.send(KAFKA_TOPIC, transaction.getHash(), transactionMapper.toBlockTransactionDto(transaction));

            log.debug("Published transaction to Kafka: {}", transaction.getHash());
        });

        updateSyncState(block.getBlock().getNumber().longValue());
    }

    /**
     * Replace the last processed block number with accepted
     *
     * @param blockNumber last processed block number
     */
    public void updateSyncState(Long blockNumber) {
        syncStateRepository.updateLastProcessedBlock(blockNumber);
    }
}
