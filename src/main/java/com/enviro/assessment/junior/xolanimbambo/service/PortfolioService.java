package com.enviro.assessment.junior.xolanimbambo.service;

import com.enviro.assessment.junior.xolanimbambo.dto.PortfolioResponse;

public interface PortfolioService {

    PortfolioResponse getPortfolio(Long investorId);
}