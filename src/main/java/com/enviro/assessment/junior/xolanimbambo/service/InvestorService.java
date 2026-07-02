package com.enviro.assessment.junior.xolanimbambo.service;

import com.enviro.assessment.junior.xolanimbambo.dto.CreateInvestorRequest;
import com.enviro.assessment.junior.xolanimbambo.dto.InvestorResponse;

import java.util.List;

public interface InvestorService {

    InvestorResponse createInvestor(CreateInvestorRequest request);

    InvestorResponse getInvestorById(Long id);

    List<InvestorResponse> getAllInvestors();

    InvestorResponse updateInvestor(Long id, CreateInvestorRequest request);

    void deleteInvestor(Long id);
}