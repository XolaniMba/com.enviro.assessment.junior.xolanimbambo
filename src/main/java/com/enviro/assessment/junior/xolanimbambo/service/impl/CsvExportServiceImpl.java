package com.enviro.assessment.junior.xolanimbambo.service.impl;

import com.enviro.assessment.junior.xolanimbambo.model.WithdrawalNotice;
import com.enviro.assessment.junior.xolanimbambo.repository.WithdrawalNoticeRepository;
import com.enviro.assessment.junior.xolanimbambo.service.CsvExportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CsvExportServiceImpl implements CsvExportService {

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public CsvExportServiceImpl(WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public String buildWithdrawalStatementCsv(Long investorId) {
        List<WithdrawalNotice> notices =
                withdrawalNoticeRepository.findByProductInvestorIdOrderByRequestedDateDesc(investorId);

        StringBuilder csv = new StringBuilder();
        csv.append("Account Number,Product Type,Amount,Balance After Withdrawal,Status,Requested Date,Reason\n");

        for (WithdrawalNotice notice : notices) {
            csv.append(escape(notice.getProduct().getAccountNumber())).append(',')
               .append(notice.getProduct().getProductType()).append(',')
               .append(notice.getAmount()).append(',')
               .append(notice.getBalanceAfterWithdrawal()).append(',')
               .append(notice.getStatus()).append(',')
               .append(notice.getRequestedDate().format(DATE_TIME_FORMAT)).append(',')
               .append(escape(notice.getReason())).append('\n');
        }

        return csv.toString();
    }

    // Guards against commas or quotes in free-text fields (like "reason")
    // breaking the CSV column structure.
    private String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}