package com.enviro.assessment.junior.xolanimbambo.service.impl;

import com.enviro.assessment.junior.xolanimbambo.dto.WithdrawalRequestDto;
import com.enviro.assessment.junior.xolanimbambo.exception.BusinessRuleViolationException;
import com.enviro.assessment.junior.xolanimbambo.exception.ProductNotFoundException;
import com.enviro.assessment.junior.xolanimbambo.model.InvestmentProduct;
import com.enviro.assessment.junior.xolanimbambo.model.Investor;
import com.enviro.assessment.junior.xolanimbambo.model.WithdrawalNotice;
import com.enviro.assessment.junior.xolanimbambo.model.enums.ProductType;
import com.enviro.assessment.junior.xolanimbambo.repository.InvestmentProductRepository;
import com.enviro.assessment.junior.xolanimbambo.repository.WithdrawalNoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceImplTest {

    @Mock
    private InvestmentProductRepository productRepository;

    @Mock
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    @InjectMocks
    private WithdrawalServiceImpl withdrawalService;

    private Investor youngInvestor;
    private Investor retiredInvestor;
    private InvestmentProduct retirementProduct;
    private InvestmentProduct discretionaryProduct;

    @BeforeEach
    void setUp() {
        youngInvestor = new Investor();
        youngInvestor.setId(1L);
        youngInvestor.setFirstName("Lindiwe");
        youngInvestor.setLastName("Dlamini");
        youngInvestor.setDateOfBirth(LocalDate.now().minusYears(34));

        retiredInvestor = new Investor();
        retiredInvestor.setId(2L);
        retiredInvestor.setFirstName("Thabo");
        retiredInvestor.setLastName("Nkosi");
        retiredInvestor.setDateOfBirth(LocalDate.now().minusYears(68));

        retirementProduct = new InvestmentProduct();
        retirementProduct.setId(1L);
        retirementProduct.setAccountNumber("RA-0001-001");
        retirementProduct.setProductType(ProductType.RETIREMENT_ANNUITY);
        retirementProduct.setBalance(new BigDecimal("100000.00"));
        retirementProduct.setInvestor(retiredInvestor);

        discretionaryProduct = new InvestmentProduct();
        discretionaryProduct.setId(2L);
        discretionaryProduct.setAccountNumber("DI-0001-001");
        discretionaryProduct.setProductType(ProductType.DISCRETIONARY_INVESTMENT);
        discretionaryProduct.setBalance(new BigDecimal("10000.00"));
        discretionaryProduct.setInvestor(youngInvestor);
    }

    @Test
    void retirementWithdrawal_rejectedWhenInvestorUnder65() {
        retirementProduct.setInvestor(youngInvestor);
        when(productRepository.findById(1L)).thenReturn(Optional.of(retirementProduct));

        WithdrawalRequestDto request = new WithdrawalRequestDto(1L, new BigDecimal("1000"), "Test");

        assertThatThrownBy(() -> withdrawalService.createWithdrawal(request))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("older than 65");
    }

    @Test
    void retirementWithdrawal_allowedWhenInvestorOver65() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(retirementProduct));
        when(withdrawalNoticeRepository.save(any(WithdrawalNotice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WithdrawalRequestDto request = new WithdrawalRequestDto(1L, new BigDecimal("1000"), "Living expenses");

        assertThat(withdrawalService.createWithdrawal(request)).isNotNull();
        assertThat(retirementProduct.getBalance()).isEqualByComparingTo("99000.00");
    }

    @Test
    void withdrawal_rejectedWhenAmountExceedsBalance() {
        when(productRepository.findById(2L)).thenReturn(Optional.of(discretionaryProduct));

        WithdrawalRequestDto request = new WithdrawalRequestDto(2L, new BigDecimal("15000"), "Too much");

        assertThatThrownBy(() -> withdrawalService.createWithdrawal(request))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("exceeds the available balance");
    }

    @Test
    void withdrawal_rejectedWhenAmountExceeds90PercentOfBalance() {
        when(productRepository.findById(2L)).thenReturn(Optional.of(discretionaryProduct));

        WithdrawalRequestDto request = new WithdrawalRequestDto(2L, new BigDecimal("9500"), "Over the cap");

        assertThatThrownBy(() -> withdrawalService.createWithdrawal(request))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("90%");
    }

    @Test
    void withdrawal_allowedWhenExactly90PercentOfBalance() {
        when(productRepository.findById(2L)).thenReturn(Optional.of(discretionaryProduct));
        when(withdrawalNoticeRepository.save(any(WithdrawalNotice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WithdrawalRequestDto request = new WithdrawalRequestDto(2L, new BigDecimal("9000"), "At the cap");

        assertThat(withdrawalService.createWithdrawal(request)).isNotNull();
    }

    @Test
    void withdrawal_throwsWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        WithdrawalRequestDto request = new WithdrawalRequestDto(99L, new BigDecimal("100"), "N/A");

        assertThatThrownBy(() -> withdrawalService.createWithdrawal(request))
                .isInstanceOf(ProductNotFoundException.class);
    }
}