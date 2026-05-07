package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class CrowdReportService {

    private final CrowdReportRepository crowdReportRepository;

    public CrowdReportService(CrowdReportRepository crowdReportRepository) {
        this.crowdReportRepository = crowdReportRepository;
    }

    public void saveCrowdReport(CrowdReport crowdReport) {
        crowdReportRepository.save(crowdReport);
    }
    public Iterable<CrowdReport> getRecentCrowdReports(int limit) {
        return crowdReportRepository.findAll().stream()
                .limit(limit)
                .sorted(Comparator.comparing(CrowdReport::timestamp).reversed())
                .toList();
    }
}
