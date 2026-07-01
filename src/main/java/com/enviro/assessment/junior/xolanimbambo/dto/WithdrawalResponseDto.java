package com.enviro.assessment.junior.xolanimbambo.dto;

import com.enviro.assessment.junior.xolanimbambo.model.enums.ProductType;
import com.enviro.assessment.junior.xolanimbambo.model.enums.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WithdrawalResponseDto {

    private Long id;
    private Long productId;
    private String accountNumber;
    private ProductType productType;
    private BigDecimal amount;
    private BigDecimal balanceAfterWithdrawal;
    private WithdrawalStatus status;
    private LocalDateTime requestedDate;
    private String reason;

    public WithdrawalResponseDto() {
    }

    public WithdrawalResponseDto(Long id, Long productId, String accountNumber, ProductType productType,
                                  BigDecimal amount, BigDecimal balanceAfterWithdrawal, WithdrawalStatus status,
                                  LocalDateTime requestedDate, String reason) {
        this.id = id;
        this.productId = productId;
        this.accountNumber = accountNumber;
        this.productType = productType;
        this.amount = amount;
        this.balanceAfterWithdrawal = balanceAfterWithdrawal;
        this.status = status;
        this.requestedDate = requestedDate;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceAfterWithdrawal() {
        return balanceAfterWithdrawal;
    }

    public void setBalanceAfterWithdrawal(BigDecimal balanceAfterWithdrawal) {
        this.balanceAfterWithdrawal = balanceAfterWithdrawal;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}