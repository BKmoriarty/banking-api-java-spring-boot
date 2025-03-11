package com.teerasak.bankingapi.repository;

import com.teerasak.bankingapi.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.id = :accountId OR t.toAccount.id = :accountId) " +
            "AND (:days IS NULL OR t.timestamp >= :startDate)")
    Page<Transaction> findByAccountId(@Param("accountId") Long accountId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("days") Integer days,
                                      Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.user.id = :userId OR t.toAccount.user.id = :userId) " +
            "AND (:days IS NULL OR t.timestamp >= :startDate)")
    Page<Transaction> findByUserId(@Param("userId") Long userId,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("days") Integer days,
                                   Pageable pageable);
}