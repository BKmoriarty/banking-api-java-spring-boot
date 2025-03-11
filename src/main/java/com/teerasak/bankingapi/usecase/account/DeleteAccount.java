package com.teerasak.bankingapi.usecase.account;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class DeleteAccount {
    private final AccountRepository accountRepository;

    public DeleteAccount(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(Long accountId, Authentication authentication) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Unauthorized: Only Admin can delete accounts");
        }

        if (account.getBalance() != 0.0) {
            throw new RuntimeException("Account balance must be 0 to delete");
        }

        accountRepository.delete(account);
    }
}