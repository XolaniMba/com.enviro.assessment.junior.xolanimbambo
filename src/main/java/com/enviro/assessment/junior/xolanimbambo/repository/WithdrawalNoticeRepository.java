package com.enviro.assessment.junior.xolanimbambo.repository;

import com.enviro.assessment.junior.xolanimbambo.model.WithdrawalNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {

    List<WithdrawalNotice> findByProductInvestorIdOrderByRequestedDateDesc(Long investorId);
}