package ua.kornello.evm_transaction_service.service;

import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;

import java.util.List;

public interface TransactionService {

    public List<BlockTransactionDto> getTransactionsByPage(Integer page, Integer pageSize);

    BlockTransactionDto getTransactionByHash(Long transactionHash);
}
