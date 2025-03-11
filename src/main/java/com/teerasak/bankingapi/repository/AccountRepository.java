package com.teerasak.bankingapi.repository;

import com.teerasak.bankingapi.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    Optional<Account> findById(Long id);
    long countByUserId(Long userId);
}
