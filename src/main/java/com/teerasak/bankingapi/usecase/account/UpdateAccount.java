package com.teerasak.bankingapi.usecase.account;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccount {
    private final AccountRepository accountRepository;

    public UpdateAccount(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(Long accountId, String newAccountName, Authentication authentication) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String username = authentication.getName();
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: Not the account owner");
        }

        account.setAccountName(newAccountName);
        accountRepository.save(account);
    }
}
