package com.teerasak.bankingapi.dto.account;

public class AccountRequest {
    private Long userId;
    private String accountName;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
}
