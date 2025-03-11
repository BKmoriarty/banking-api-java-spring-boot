package com.teerasak.bankingapi.controller;

import com.teerasak.bankingapi.domain.Transaction;
import com.teerasak.bankingapi.dto.transfer.TransferRequest;
import com.teerasak.bankingapi.dto.transfer.TransferResponse;
import com.teerasak.bankingapi.usecase.transfer.TransferMoney;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {
    private final TransferMoney transferMoney;

    public TransferController(TransferMoney transferMoney) {
        this.transferMoney = transferMoney;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request, Authentication authentication) {
        Transaction transaction = transferMoney.execute(request.getFromAccountId(), request.getToAccountId(),
                request.getAmount(), authentication);
        TransferResponse response = new TransferResponse(transaction.getId(),
                transaction.getFromAccount().getId(),
                transaction.getToAccount().getId(),
                transaction.getAmount(),
                transaction.getTimestamp());
        return ResponseEntity.ok(response);
    }
}