package com.teerasak.bankingapi.usecase.account;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.domain.User;
import com.teerasak.bankingapi.repository.AccountRepository;
import com.teerasak.bankingapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CreateAccount {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public CreateAccount(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public Account execute(Long userId, String accountName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (accountRepository.countByUserId(userId) >= 3) {
            throw new RuntimeException("User already has maximum 3 accounts");
        }

        String accountNumber = generateAccountNumber();
        Account account = new Account(user, accountNumber, accountName, 0.0);
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        Random rand = new Random();
        String part1 = String.format("%04d", rand.nextInt(10000));
        String part2 = String.format("%04d", rand.nextInt(10000));
        return "ACC-" + part1 + "-" + part2; // e.g., ACC-1234-5678
    }
}