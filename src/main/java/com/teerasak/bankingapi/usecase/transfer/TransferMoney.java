package com.teerasak.bankingapi.usecase.transfer;

import com.teerasak.bankingapi.domain.Account;
import com.teerasak.bankingapi.domain.Transaction;
import com.teerasak.bankingapi.dto.notification.TransferNotification;
import com.teerasak.bankingapi.repository.AccountRepository;
import com.teerasak.bankingapi.repository.TransactionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferMoney {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public TransferMoney(AccountRepository accountRepository, TransactionRepository transactionRepository, RabbitTemplate rabbitTemplate) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Transaction execute(Long fromAccountId, Long toAccountId, Double amount, Authentication authentication) {
        // ตรวจสอบบัญชีต้นทางและปลายทาง
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        // ตรวจสอบเจ้าของบัญชีต้นทาง
        String username = authentication.getName();
        if (!fromAccount.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: Not the owner of source account");
        }

        // ตรวจสอบจำนวนเงิน
        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        // ตรวจสอบยอดคงเหลือ
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance in source account");
        }

        // ดำเนินการโอนเงิน
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        // บันทึกการเปลี่ยนแปลงบัญชี
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // บันทึกธุรกรรม
        Transaction transaction = new Transaction(fromAccount, toAccount, amount, LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);

        // ส่ง message ไป RabbitMQ
        TransferNotification notification = new TransferNotification(
                savedTransaction.getId(),
                fromAccountId,
                toAccountId,
                amount,
                savedTransaction.getTimestamp()
        );
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notification);

        return savedTransaction;
    }
}