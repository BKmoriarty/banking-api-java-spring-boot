package com.teerasak.bankingapi.controller;

import com.teerasak.bankingapi.domain.Transaction;
import com.teerasak.bankingapi.dto.transaction.PaginatedTransactionResponse;
import com.teerasak.bankingapi.dto.transaction.TransactionResponse;
import com.teerasak.bankingapi.dto.transfer.TransferRequest;
import com.teerasak.bankingapi.usecase.transactions.GetAccountTransactions;
import com.teerasak.bankingapi.usecase.transactions.GetUserTransactions;
import com.teerasak.bankingapi.usecase.transfer.TransferMoney;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransferMoney transferMoney;
    private final GetAccountTransactions getAccountTransactions;
    private final GetUserTransactions getUserTransactions;

    public TransactionController(TransferMoney transferMoney,
                                 GetAccountTransactions getAccountTransactions,
                                 GetUserTransactions getUserTransactions) {
        this.transferMoney = transferMoney;
        this.getAccountTransactions = getAccountTransactions;
        this.getUserTransactions = getUserTransactions;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransferRequest request,
                                                        Authentication authentication) {
        Transaction transaction = transferMoney.execute(request.getFromAccountId(), request.getToAccountId(),
                request.getAmount(), authentication);
        TransactionResponse response = new TransactionResponse(transaction.getId(),
                transaction.getFromAccount().getId(),
                transaction.getToAccount().getId(),
                transaction.getAmount(),
                transaction.getTimestamp());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<PaginatedTransactionResponse> getAccountTransactions(
            @PathVariable Long accountId,
            @RequestParam(required = false) Integer days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            Authentication authentication) {
        Page<Transaction> transactionPage = getAccountTransactions.execute(accountId, days, page, size, sort, authentication);
        List<TransactionResponse> transactions = transactionPage.getContent().stream()
                .map(t -> new TransactionResponse(t.getId(), t.getFromAccount().getId(),
                        t.getToAccount().getId(), t.getAmount(), t.getTimestamp()))
                .collect(Collectors.toList());
        PaginatedTransactionResponse response = new PaginatedTransactionResponse(
                transactions, transactionPage.getTotalElements(), transactionPage.getTotalPages(),
                transactionPage.getNumber(), transactionPage.getSize());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PaginatedTransactionResponse> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            Authentication authentication) {
        Page<Transaction> transactionPage = getUserTransactions.execute(userId, days, page, size, sort, authentication);
        List<TransactionResponse> transactions = transactionPage.getContent().stream()
                .map(t -> new TransactionResponse(t.getId(), t.getFromAccount().getId(),
                        t.getToAccount().getId(), t.getAmount(), t.getTimestamp()))
                .collect(Collectors.toList());
        PaginatedTransactionResponse response = new PaginatedTransactionResponse(
                transactions, transactionPage.getTotalElements(), transactionPage.getTotalPages(),
                transactionPage.getNumber(), transactionPage.getSize());
        return ResponseEntity.ok(response);
    }
}