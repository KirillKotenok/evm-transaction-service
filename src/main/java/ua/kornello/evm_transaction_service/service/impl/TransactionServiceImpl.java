package ua.kornello.evm_transaction_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;
import ua.kornello.evm_transaction_service.entity.BlockTransaction;
import ua.kornello.evm_transaction_service.exeption.TransactionNotFoundException;
import ua.kornello.evm_transaction_service.mapper.BlockTransactionMapper;
import ua.kornello.evm_transaction_service.repo.TransactionRepository;
import ua.kornello.evm_transaction_service.service.TransactionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final BlockTransactionMapper blockTransactionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BlockTransactionDto> getTransactionsByPage(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<BlockTransaction> blockTransactionPage = repository.findAll(pageRequest);
        return blockTransactionPage.get().map(blockTransactionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BlockTransactionDto getTransactionByHash(Long transactionHash) {
        BlockTransaction blockTransaction = repository.findById(transactionHash)
                .orElseThrow(() -> new TransactionNotFoundException("Cannot find transaction with hash: {}".formatted(transactionHash)));
        return blockTransactionMapper.toDto(blockTransaction);
    }
}
