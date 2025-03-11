package com.teerasak.bankingapi.dto.transaction;

import java.util.List;

public class PaginatedTransactionResponse {
    private List<TransactionResponse> transactions;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public PaginatedTransactionResponse(List<TransactionResponse> transactions, long totalElements,
                                        int totalPages, int currentPage, int pageSize) {
        this.transactions = transactions;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getters and Setters
    public List<TransactionResponse> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionResponse> transactions) { this.transactions = transactions; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}