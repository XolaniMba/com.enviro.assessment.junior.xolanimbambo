package com.enviro.assessment.junior.xolanimbambo.dto;

import java.time.LocalDate;

public class InvestorResponse {

    private Long id;
    private String fullName;
    private String idNumber;
    private String email;
    private LocalDate dateOfBirth;
    private int age;

    public InvestorResponse() {
    }

    public InvestorResponse(Long id, String fullName, String idNumber, String email,
                             LocalDate dateOfBirth, int age) {
        this.id = id;
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}