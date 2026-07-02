package com.enviro.assessment.junior.xolanimbambo.controller;

import com.enviro.assessment.junior.xolanimbambo.dto.WithdrawalRequestDto;
import com.enviro.assessment.junior.xolanimbambo.dto.WithdrawalResponseDto;
import com.enviro.assessment.junior.xolanimbambo.service.CsvExportService;
import com.enviro.assessment.junior.xolanimbambo.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;
    private final CsvExportService csvExportService;

    public WithdrawalController(WithdrawalService withdrawalService, CsvExportService csvExportService) {
        this.withdrawalService = withdrawalService;
        this.csvExportService = csvExportService;
    }

    // POST /api/withdrawals
    // Creates a new withdrawal notice. @Valid triggers Bean Validation on
    // WithdrawalRequestDto; business rule failures are thrown as
    // BusinessRuleViolationException and handled by GlobalExceptionHandler.
    @PostMapping
    public ResponseEntity<WithdrawalResponseDto> createWithdrawal(@Valid @RequestBody WithdrawalRequestDto request) {
        WithdrawalResponseDto response = withdrawalService.createWithdrawal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/withdrawals/investor/{investorId}
    // Returns the full withdrawal history for an investor, most recent first.
    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<WithdrawalResponseDto>> getHistory(@PathVariable Long investorId) {
        return ResponseEntity.ok(withdrawalService.getHistoryForInvestor(investorId));
    }

    // GET /api/withdrawals/investor/{investorId}/export
    // Streams back a downloadable CSV of the investor's withdrawal history.
    @GetMapping("/investor/{investorId}/export")
    public ResponseEntity<String> exportCsv(@PathVariable Long investorId) {
        String csv = csvExportService.buildWithdrawalStatementCsv(investorId);
        String filename = "withdrawal-statement-" + investorId + "-"
                + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}