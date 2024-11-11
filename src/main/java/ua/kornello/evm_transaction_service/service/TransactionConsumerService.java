package ua.kornello.evm_transaction_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;
import ua.kornello.evm_transaction_service.entity.BlockTransaction;
import ua.kornello.evm_transaction_service.mapper.BlockTransactionMapper;
import ua.kornello.evm_transaction_service.repo.TransactionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionConsumerService {

    private final TransactionRepository transactionRepository;
    private final BlockTransactionMapper blockTransactionMapper;

    /**
     * Listener that received kafka messages and save to db
     *
     * @param transaction processing transaction
     */
    @Transactional
    @KafkaListener(topics = "transactions", groupId = "transaction-group")
    public void consumeTransaction(BlockTransactionDto transaction) {
        log.debug("Start processing transaction {} of block {}", transaction.value(), transaction.blockNumber());
        try {
            BlockTransaction transactionEntity = blockTransactionMapper.toEntity(transaction);
            transactionRepository.save(transactionEntity);
        } catch (DataIntegrityViolationException e) {
            // Handle the case where the entity already exists
            log.debug("Transaction with HASH {} already exists, skipping", transaction.hash());
        }

        log.debug("Transaction {} of block {} successfully processed", transaction.value(), transaction.blockNumber());
    }
}
