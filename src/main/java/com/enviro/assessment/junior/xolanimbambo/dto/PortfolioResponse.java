package com.enviro.assessment.junior.xolanimbambo.dto;

import java.util.List;

public class PortfolioResponse {

    private Long investorId;
    private String fullName;
    private String idNumber;
    private String email;
    private int age;
    private List<ProductDto> products;

    public PortfolioResponse() {
    }

    public PortfolioResponse(Long investorId, String fullName, String idNumber, String email,
                              int age, List<ProductDto> products) {
        this.investorId = investorId;
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.email = email;
        this.age = age;
        this.products = products;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}