package com.enviro.assessment.junior.xolanimbambo.service.impl;

import com.enviro.assessment.junior.xolanimbambo.dto.WithdrawalRequestDto;
import com.enviro.assessment.junior.xolanimbambo.dto.WithdrawalResponseDto;
import com.enviro.assessment.junior.xolanimbambo.exception.BusinessRuleViolationException;
import com.enviro.assessment.junior.xolanimbambo.exception.ProductNotFoundException;
import com.enviro.assessment.junior.xolanimbambo.model.InvestmentProduct;
import com.enviro.assessment.junior.xolanimbambo.model.Investor;
import com.enviro.assessment.junior.xolanimbambo.model.WithdrawalNotice;
import com.enviro.assessment.junior.xolanimbambo.model.enums.ProductType;
import com.enviro.assessment.junior.xolanimbambo.model.enums.WithdrawalStatus;
import com.enviro.assessment.junior.xolanimbambo.repository.InvestmentProductRepository;
import com.enviro.assessment.junior.xolanimbambo.repository.WithdrawalNoticeRepository;
import com.enviro.assessment.junior.xolanimbambo.service.WithdrawalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    // 90% cap on withdrawals, expressed as a BigDecimal multiplier.
    private static final BigDecimal MAX_WITHDRAWAL_PERCENTAGE = new BigDecimal("0.90");

    private final InvestmentProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public WithdrawalServiceImpl(InvestmentProductRepository productRepository,
                                  WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.productRepository = productRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    @Override
    @Transactional
    public WithdrawalResponseDto createWithdrawal(WithdrawalRequestDto request) {
        InvestmentProduct product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Investment product not found with id: " + request.getProductId()));

        validateWithdrawal(product, request.getAmount());

        BigDecimal newBalance = product.getBalance().subtract(request.getAmount());
        product.setBalance(newBalance);
        productRepository.save(product);

        WithdrawalNotice notice = new WithdrawalNotice(
                product,
                request.getAmount(),
                newBalance,
                WithdrawalStatus.APPROVED,
                LocalDateTime.now(),
                request.getReason()
        );

        WithdrawalNotice saved = withdrawalNoticeRepository.save(notice);
        return toDto(saved);
    }

    /**
     * Runs all three business rules in order and throws on the first one
     * that fails, with a clear message the frontend can show the user.
     */
    private void validateWithdrawal(InvestmentProduct product, BigDecimal amount) {
        Investor investor = product.getInvestor();

        // Rule 1: retirement withdrawals only allowed if age > 65
        if (product.getProductType() == ProductType.RETIREMENT_ANNUITY && investor.getAge() <= 65) {
            throw new BusinessRuleViolationException(
                    "Retirement withdrawals are only allowed for investors older than 65. "
                            + "Investor is currently " + investor.getAge() + " years old.");
        }

        // Rule 2: withdrawal must not exceed balance
        if (amount.compareTo(product.getBalance()) > 0) {
            throw new BusinessRuleViolationException(
                    "Withdrawal amount (" + amount + ") exceeds the available balance ("
                            + product.getBalance() + ").");
        }

        // Rule 3: withdrawal must not exceed 90% of balance
        BigDecimal maxAllowed = product.getBalance()
                .multiply(MAX_WITHDRAWAL_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);

        if (amount.compareTo(maxAllowed) > 0) {
            throw new BusinessRuleViolationException(
                    "Withdrawal amount (" + amount + ") exceeds 90% of the available balance. "
                            + "Maximum allowed withdrawal is " + maxAllowed + ".");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<WithdrawalResponseDto> getHistoryForInvestor(Long investorId) {
        return withdrawalNoticeRepository
                .findByProductInvestorIdOrderByRequestedDateDesc(investorId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private WithdrawalResponseDto toDto(WithdrawalNotice notice) {
        InvestmentProduct product = notice.getProduct();
        return new WithdrawalResponseDto(
                notice.getId(),
                product.getId(),
                product.getAccountNumber(),
                product.getProductType(),
                notice.getAmount(),
                notice.getBalanceAfterWithdrawal(),
                notice.getStatus(),
                notice.getRequestedDate(),
                notice.getReason()
        );
    }
}