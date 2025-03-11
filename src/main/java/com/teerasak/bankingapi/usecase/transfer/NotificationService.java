package com.teerasak.bankingapi.usecase.transfer;

import com.teerasak.bankingapi.dto.notification.TransferNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void receiveTransferNotification(TransferNotification notification) {
        logger.info("Transfer successful - Transaction ID: {}, From Account: {}, To Account: {}, Amount: {}, Timestamp: {}",
                notification.getTransactionId(),
                notification.getFromAccountId(),
                notification.getToAccountId(),
                notification.getAmount(),
                notification.getTimestamp());
    }
}