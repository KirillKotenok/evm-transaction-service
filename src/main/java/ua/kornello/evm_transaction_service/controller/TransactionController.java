package ua.kornello.evm_transaction_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;
import ua.kornello.evm_transaction_service.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<BlockTransactionDto>> getAllTransactions(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer pageSize) {
        return ResponseEntity.ok(transactionService.getTransactionsByPage(page, pageSize));
    }

    @GetMapping("/{transactionHash}")
    public ResponseEntity<BlockTransactionDto> getTransactionById(@PathVariable Long transactionHash) {
        return ResponseEntity.ok(transactionService.getTransactionByHash(transactionHash));
    }
}
