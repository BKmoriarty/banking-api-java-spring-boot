package com.teerasak.bankingapi.usecase.transactions;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.domain.Transaction;
import com.teerasak.bankingapi.repository.AccountRepository;
import com.teerasak.bankingapi.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetAccountTransactions {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public GetAccountTransactions(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Page<Transaction> execute(Long accountId, Integer days, int page, int size, String sort,
                                     Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!isAdmin && !account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: Can only view own account transactions");
        }

        LocalDateTime startDate = (days != null) ? LocalDateTime.now().minusDays(days) : null;
        Pageable pageable = buildPageable(page, size, sort);
        Page<Transaction> transactions = transactionRepository.findByAccountId(accountId, startDate, days, pageable);

        if (transactions.isEmpty()) {
            throw new RuntimeException("No transactions found for account ID: " + accountId);
        }

        return transactions;
    }

    private Pageable buildPageable(int page, int size, String sort) {
        if (sort == null || sort.isEmpty()) {
            return PageRequest.of(page, size, Sort.by("timestamp").descending());
        }
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, field));
    }
}