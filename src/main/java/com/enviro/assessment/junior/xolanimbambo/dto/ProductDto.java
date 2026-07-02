package com.enviro.assessment.junior.xolanimbambo.dto;

import com.enviro.assessment.junior.xolanimbambo.model.enums.ProductType;

import java.math.BigDecimal;

public class ProductDto {

    private Long id;
    private String accountNumber;
    private ProductType productType;
    private BigDecimal balance;

    public ProductDto() {
    }

    public ProductDto(Long id, String accountNumber, ProductType productType, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.productType = productType;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}