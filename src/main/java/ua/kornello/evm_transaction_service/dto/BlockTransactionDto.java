package ua.kornello.evm_transaction_service.dto;

import java.math.BigInteger;
import java.sql.Timestamp;

public record BlockTransactionDto(
        String hash,
        String fromAddress,
        String toAddress,
        BigInteger value,
        BigInteger blockNumber,
        String rawBlockNumber,
        Timestamp createdAt) {
}
