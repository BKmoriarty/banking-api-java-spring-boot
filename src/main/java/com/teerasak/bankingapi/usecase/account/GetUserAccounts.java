package com.teerasak.bankingapi.usecase.account;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserAccounts {
    private final AccountRepository accountRepository;

    public GetUserAccounts(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> execute(Long userId, Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<Account> accounts = accountRepository.findByUserId(userId);

        if (accounts.isEmpty()) {
            throw new RuntimeException("No accounts found for user ID: " + userId);
        }

        // ตรวจสอบสิทธิ์: ผู้ใช้ทั่วไปเห็นเฉพาะบัญชีตัวเอง, Admin เห็นได้หมด
        if (!isAdmin) {
            boolean isOwner = accounts.stream()
                    .allMatch(account -> account.getUser().getUsername().equals(username));
            if (!isOwner) {
                throw new RuntimeException("Unauthorized: Can only view own accounts");
            }
        }

        return accounts;
    }
}
