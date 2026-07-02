package com.enviro.assessment.junior.xolanimbambo.controller;

import com.enviro.assessment.junior.xolanimbambo.dto.CreateInvestorRequest;
import com.enviro.assessment.junior.xolanimbambo.dto.InvestorResponse;
import com.enviro.assessment.junior.xolanimbambo.service.InvestorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investors")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PostMapping
    public ResponseEntity<InvestorResponse> createInvestor(@Valid @RequestBody CreateInvestorRequest request) {
        InvestorResponse response = investorService.createInvestor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestorResponse> getInvestorById(@PathVariable Long id) {
        return ResponseEntity.ok(investorService.getInvestorById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvestorResponse>> getAllInvestors() {
        return ResponseEntity.ok(investorService.getAllInvestors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestorResponse> updateInvestor(@PathVariable Long id,
                                                             @Valid @RequestBody CreateInvestorRequest request) {
        return ResponseEntity.ok(investorService.updateInvestor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestor(@PathVariable Long id) {
        investorService.deleteInvestor(id);
        return ResponseEntity.noContent().build();
    }
}