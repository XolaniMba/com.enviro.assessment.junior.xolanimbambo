package com.enviro.assessment.junior.xolanimbambo.model;

import com.enviro.assessment.junior.xolanimbambo.model.enums.WithdrawalStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawal_notices")
public class WithdrawalNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private InvestmentProduct product;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfterWithdrawal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalStatus status;

    @Column(nullable = false)
    private LocalDateTime requestedDate;

    private String reason;

    public WithdrawalNotice() {
    }

    public WithdrawalNotice(InvestmentProduct product, BigDecimal amount, BigDecimal balanceAfterWithdrawal,
                             WithdrawalStatus status, LocalDateTime requestedDate, String reason) {
        this.product = product;
        this.amount = amount;
        this.balanceAfterWithdrawal = balanceAfterWithdrawal;
        this.status = status;
        this.requestedDate = requestedDate;
        this.reason = reason;
    }

    // --- Getters and setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvestmentProduct getProduct() {
        return product;
    }

    public void setProduct(InvestmentProduct product) {
        this.product = product;
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