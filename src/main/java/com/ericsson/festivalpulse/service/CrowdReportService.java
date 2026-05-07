package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrowdReportService {

    private final CrowdReportRepository crowdReportRepository;

    public CrowdReportService(CrowdReportRepository crowdReportRepository) {
        this.crowdReportRepository = crowdReportRepository;
    }

    public List<CrowdReport> getRecentCrowdReports() {
        return crowdReportRepository.findTop10ByOrderByTimestampDesc();
    }

    public CrowdReport saveCrowdReport(CrowdReport crowdReport) {
        return crowdReportRepository.save(crowdReport);
    }

    public Optional<CrowdReport> getLatestReportForArea(FestivalArea area) {
        return crowdReportRepository.findFirstByAreaOrderByTimestampDesc(area);
    }

    public long countReports() {
        return crowdReportRepository.count();
    }
}
