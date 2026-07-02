package com.enviro.assessment.junior.xolanimbambo.service.impl;

import com.enviro.assessment.junior.xolanimbambo.dto.CreateInvestorRequest;
import com.enviro.assessment.junior.xolanimbambo.dto.InvestorResponse;
import com.enviro.assessment.junior.xolanimbambo.exception.InvestorNotFoundException;
import com.enviro.assessment.junior.xolanimbambo.exception.validation.AgeValidator;
import com.enviro.assessment.junior.xolanimbambo.model.Investor;
import com.enviro.assessment.junior.xolanimbambo.repository.InvestorRepository;
import com.enviro.assessment.junior.xolanimbambo.service.InvestorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestorServiceImpl implements InvestorService {

    private final InvestorRepository investorRepository;

    public InvestorServiceImpl(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    @Override
    @Transactional
    public InvestorResponse createInvestor(CreateInvestorRequest request) {
        AgeValidator.validate(request.getDateOfBirth());

        Investor investor = new Investor();
        investor.setFirstName(request.getFirstName());
        investor.setLastName(request.getLastName());
        investor.setIdNumber(request.getIdNumber());
        investor.setEmail(request.getEmail());
        investor.setDateOfBirth(request.getDateOfBirth());

        Investor saved = investorRepository.save(investor);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InvestorResponse getInvestorById(Long id) {
        Investor investor = investorRepository.findById(id)
                .orElseThrow(() -> new InvestorNotFoundException("Investor not found with id: " + id));
        return toResponse(investor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvestorResponse> getAllInvestors() {
        return investorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvestorResponse updateInvestor(Long id, CreateInvestorRequest request) {
        Investor investor = investorRepository.findById(id)
                .orElseThrow(() -> new InvestorNotFoundException("Investor not found with id: " + id));

        AgeValidator.validate(request.getDateOfBirth());

        investor.setFirstName(request.getFirstName());
        investor.setLastName(request.getLastName());
        investor.setIdNumber(request.getIdNumber());
        investor.setEmail(request.getEmail());
        investor.setDateOfBirth(request.getDateOfBirth());

        Investor saved = investorRepository.save(investor);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteInvestor(Long id) {
        if (!investorRepository.existsById(id)) {
            throw new InvestorNotFoundException("Investor not found with id: " + id);
        }
        investorRepository.deleteById(id);
    }

    private InvestorResponse toResponse(Investor investor) {
        return new InvestorResponse(
                investor.getId(),
                investor.getFullName(),
                investor.getIdNumber(),
                investor.getEmail(),
                investor.getDateOfBirth(),
                investor.getAge()
        );
    }
}