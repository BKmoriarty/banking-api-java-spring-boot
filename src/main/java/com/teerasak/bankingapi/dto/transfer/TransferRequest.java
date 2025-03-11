package com.teerasak.bankingapi.dto.transfer;

public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;

    // Getters and Setters
    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}