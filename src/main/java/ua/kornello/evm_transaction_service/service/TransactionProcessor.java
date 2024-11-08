package ua.kornello.evm_transaction_service.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import ua.kornello.evm_transaction_service.entity.NodeTransaction;
import ua.kornello.evm_transaction_service.entity.SyncState;
import ua.kornello.evm_transaction_service.repo.SyncStateRepository;
import ua.kornello.evm_transaction_service.repo.TransactionRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TransactionProcessor {

    private final EvmService evmService;
    private final TransactionRepository transactionRepository;
    private final SyncStateRepository syncStateRepository;

    private Long lastProcessedBlock;

    public TransactionProcessor(EvmService evmService, TransactionRepository transactionRepository,
                                SyncStateRepository syncStateRepository) {
        this.evmService = evmService;
        this.transactionRepository = transactionRepository;
        this.syncStateRepository = syncStateRepository;
    }

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        // Load the last processed block from the SyncState repository
        SyncState syncState = syncStateRepository.findTopByOrderByIdDesc().orElse(new SyncState());
        lastProcessedBlock = syncState.getLastProcessedBlock();
    }

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void processNewTransactions() throws IOException {
        Long latestBlockNumber = evmService.getLatestBlockNumber();

        // Process new blocks since the last processed one
        for (long blockNumber = lastProcessedBlock + 1; blockNumber <= latestBlockNumber; blockNumber++) {
            log.info("Processing block {}", blockNumber);

            List<NodeTransaction> transactions = fetchTransactions(blockNumber);
            processTransactions(transactions);
            updateSyncState(blockNumber);
        }
    }

    /**
     * Fetches transactions from a specific block number.
     *
     * @param blockNumber the block number to fetch transactions from
     * @return List of transactions in the block
     * @throws IOException if there is an issue connecting to the blockchain node
     */
    private List<NodeTransaction> fetchTransactions(long blockNumber) throws IOException {
        List<NodeTransaction> transactions = new ArrayList<>();

        EthBlock block = evmService.getBlockByNumber(blockNumber);

        for (EthBlock.TransactionResult<?> transactionResult : block.getBlock().getTransactions()) {
            EthBlock.TransactionObject tx = (EthBlock.TransactionObject) transactionResult.get();

            NodeTransaction transaction = new NodeTransaction();
            transaction.setHash(tx.getHash());
            transaction.setFromAddress(tx.getFrom());
            transaction.setToAddress(tx.getTo());
            transaction.setValue(tx.getValue());
            transaction.setBlockNumber(tx.getBlockNumber());
            transaction.setRawBlockNumber(tx.getBlockNumberRaw());
            transaction.setTimestamp(new Timestamp(block.getBlock().getTimestamp().longValue()));

            transactions.add(transaction);
        }

        return transactions;
    }

    /**
     * Processes each transaction by saving it to the database.
     *
     * @param transactions List of transactions to process
     */
    private void processTransactions(List<NodeTransaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    /**
     * Updates the SyncState to reflect the last processed block.
     *
     * @param blockNumber The latest processed block number
     */
    private void updateSyncState(Long blockNumber) {
        lastProcessedBlock = blockNumber;

        SyncState syncState = new SyncState();
        syncState.setLastProcessedBlock(blockNumber);
        syncStateRepository.save(syncState);
    }
}
