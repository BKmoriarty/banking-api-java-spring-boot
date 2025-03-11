package com.teerasak.bankingapi.usecase.account;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class GetAccount {
    private final AccountRepository accountRepository;

    public GetAccount(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(Long accountId, Authentication authentication) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: Not the account owner");
        }

        return account;
    }
}
