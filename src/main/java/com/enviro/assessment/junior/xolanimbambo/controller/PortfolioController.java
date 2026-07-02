package com.enviro.assessment.junior.xolanimbambo.controller;

import com.enviro.assessment.junior.xolanimbambo.dto.PortfolioResponse;
import com.enviro.assessment.junior.xolanimbambo.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/investors")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    // GET /api/investors/{investorId}/portfolio
    // Returns investor details plus the list of investment products that
    // make up their portfolio.
    @GetMapping("/{investorId}/portfolio")
    public ResponseEntity<PortfolioResponse> getPortfolio(@PathVariable Long investorId) {
        return ResponseEntity.ok(portfolioService.getPortfolio(investorId));
    }
}