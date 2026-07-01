package com.enviro.assessment.junior.xolanimbambo.repository;

import com.enviro.assessment.junior.xolanimbambo.model.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {

    List<InvestmentProduct> findByInvestorId(Long investorId);
}