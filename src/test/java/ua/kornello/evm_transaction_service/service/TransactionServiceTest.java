package ua.kornello.evm_transaction_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;
import ua.kornello.evm_transaction_service.entity.BlockTransaction;
import ua.kornello.evm_transaction_service.exeption.TransactionNotFoundException;
import ua.kornello.evm_transaction_service.mapper.BlockTransactionMapper;
import ua.kornello.evm_transaction_service.repo.TransactionRepository;
import ua.kornello.evm_transaction_service.service.impl.TransactionServiceImpl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private BlockTransactionMapper blockTransactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private BlockTransaction blockTransaction;
    private BlockTransactionDto blockTransactionDto;

    @BeforeEach
    void setUp() {
        prepareBlockTransaction();
    }


    @Test
    void whenGetAllTransactionsByPageThenReturn() {
        int page = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<BlockTransaction> transactions = List.of(blockTransaction);
        Page<BlockTransaction> transactionPage = new PageImpl<>(transactions);

        when(repository.findAll(pageRequest)).thenReturn(transactionPage);
        when(blockTransactionMapper.toDto(any(BlockTransaction.class))).thenReturn(blockTransactionDto);

        List<BlockTransactionDto> result = transactionService.getTransactionsByPage(page, pageSize);

        assertEquals(1, result.size());
        assertEquals(blockTransactionDto, result.getFirst());
        verify(repository, times(1)).findAll(pageRequest);
        verify(blockTransactionMapper, times(1)).toDto(blockTransaction);
    }

    @Test
    void whenGetTransactionByHashThenReturn() {
        Long transactionHash = 123L;

        when(repository.findById(transactionHash)).thenReturn(Optional.of(blockTransaction));
        when(blockTransactionMapper.toDto(blockTransaction)).thenReturn(blockTransactionDto);

        BlockTransactionDto result = transactionService.getTransactionByHash(transactionHash);

        assertEquals(blockTransactionDto, result);
        verify(repository, times(1)).findById(transactionHash);
        verify(blockTransactionMapper, times(1)).toDto(blockTransaction);
    }

    @Test
    void whenGetTransactionByHashThatNotExistThenThrowTransactionNotFoundException() {
        Long transactionHash = 123L;

        when(repository.findById(transactionHash)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionByHash(transactionHash));
        verify(repository, times(1)).findById(transactionHash);
        verifyNoInteractions(blockTransactionMapper);
    }

    private void prepareBlockTransaction() {
        String hash = "sampleHash";
        String from = "sampleFromAddress";
        String to = "sampleToAddress";
        BigInteger value = BigInteger.valueOf(1000);
        BigInteger blockNumber = BigInteger.valueOf(12345);
        String rawBlockNumber = "rawBlock12345";
        Timestamp createdAt = Timestamp.valueOf("2024-11-11 10:00:00");

        blockTransaction = new BlockTransaction();
        blockTransaction.setHash(hash);
        blockTransaction.setFromAddress(from);
        blockTransaction.setToAddress(to);
        blockTransaction.setValue(value);
        blockTransaction.setBlockNumber(blockNumber.longValue());
        blockTransaction.setRawBlockNumber(rawBlockNumber);
        blockTransaction.setCreatedAt(createdAt);

        blockTransactionDto = new BlockTransactionDto(hash,
                from,
                to,
                value,
                blockNumber,
                rawBlockNumber,
                createdAt);
    }
}