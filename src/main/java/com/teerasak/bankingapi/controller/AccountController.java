package com.teerasak.bankingapi.controller;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.dto.account.AccountRequest;
import com.teerasak.bankingapi.dto.account.AccountResponse;
import com.teerasak.bankingapi.usecase.account.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final CreateAccount createAccount;
    private final GetAccount getAccount;
    private final UpdateAccount updateAccount;
    private final DeleteAccount deleteAccount;
    private final GetUserAccounts getUserAccounts;

    public AccountController(CreateAccount createAccount, GetAccount getAccount,
                             UpdateAccount updateAccount, DeleteAccount deleteAccount, GetUserAccounts getUserAccounts) {
        this.createAccount = createAccount;
        this.getAccount = getAccount;
        this.updateAccount = updateAccount;
        this.deleteAccount = deleteAccount;
        this.getUserAccounts = getUserAccounts;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        Account account = createAccount.execute(request.getUserId(), request.getAccountName());
        return ResponseEntity.ok(new AccountResponse(account.getId(), account.getAccountNumber(),
                account.getAccountName(), account.getBalance()));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId, Authentication authentication) {
        Account account = getAccount.execute(accountId, authentication);
        return ResponseEntity.ok(new AccountResponse(account.getId(), account.getAccountNumber(),
                account.getAccountName(), account.getBalance()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(@PathVariable Long userId,
                                                                 Authentication authentication) {
        List<Account> accounts = getUserAccounts.execute(userId, authentication);
        List<AccountResponse> response = accounts.stream()
                .map(account -> new AccountResponse(account.getId(), account.getAccountNumber(),
                        account.getAccountName(), account.getBalance()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<String> updateAccount(@PathVariable Long accountId,
                                                @RequestBody AccountRequest request,
                                                Authentication authentication) {
        updateAccount.execute(accountId, request.getAccountName(), authentication);
        return ResponseEntity.ok("Account updated successfully");
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId, Authentication authentication) {
        deleteAccount.execute(accountId, authentication);
        return ResponseEntity.ok("Account deleted successfully");
    }
}