package com.enviro.assessment.junior.xolanimbambo.service;

import com.enviro.assessment.junior.xolanimbambo.dto.WithdrawalRequestDto;
import com.enviro.assessment.junior.xolanimbambo.dto.WithdrawalResponseDto;

import java.util.List;

public interface WithdrawalService {

    WithdrawalResponseDto createWithdrawal(WithdrawalRequestDto request);

    List<WithdrawalResponseDto> getHistoryForInvestor(Long investorId);
}