package com.enviro.assessment.junior.xolanimbambo.service.impl;

import com.enviro.assessment.junior.xolanimbambo.dto.PortfolioResponse;
import com.enviro.assessment.junior.xolanimbambo.dto.ProductDto;
import com.enviro.assessment.junior.xolanimbambo.exception.InvestorNotFoundException;
import com.enviro.assessment.junior.xolanimbambo.model.Investor;
import com.enviro.assessment.junior.xolanimbambo.model.InvestmentProduct;
import com.enviro.assessment.junior.xolanimbambo.repository.InvestmentProductRepository;
import com.enviro.assessment.junior.xolanimbambo.repository.InvestorRepository;
import com.enviro.assessment.junior.xolanimbambo.service.PortfolioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final InvestorRepository investorRepository;
    private final InvestmentProductRepository productRepository;

    public PortfolioServiceImpl(InvestorRepository investorRepository,
                                 InvestmentProductRepository productRepository) {
        this.investorRepository = investorRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolio(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvestorNotFoundException(
                        "Investor not found with id: " + investorId));

        List<ProductDto> products = productRepository.findByInvestorId(investorId).stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());

        return new PortfolioResponse(
                investor.getId(),
                investor.getFullName(),
                investor.getIdNumber(),
                investor.getEmail(),
                investor.getAge(),
                products
        );
    }

    private ProductDto toProductDto(InvestmentProduct product) {
        return new ProductDto(
                product.getId(),
                product.getAccountNumber(),
                product.getProductType(),
                product.getBalance()
        );
    }
}