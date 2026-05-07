package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public CrowdReport submitReport(CrowdReport request, FestivalAreaService areaService, CrowdAlertService alertService) {
        if (request.getArea() == null || request.getArea().getId() == null) {
            throw new IllegalArgumentException("Area ID is required");
        }

        FestivalArea area = areaService.getAreaById(request.getArea().getId());
        if (area == null) {
            throw new IllegalArgumentException("Area not found");
        }

        LocalDateTime timestamp = request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now();

        CrowdReport toSave = new CrowdReport(
                area,
                request.getCrowdLevel(),
                timestamp,
                request.getShortNote()
        );

        CrowdReport savedReport = saveCrowdReport(toSave);

        if (savedReport.getCrowdLevel() == CrowdLevel.FULL) {
            List<CrowdAlert> activeAlerts = alertService.getActiveAlertsByArea(area);
            if (activeAlerts.isEmpty()) {
                CrowdAlert alert = new CrowdAlert();
                alert.setArea(area);
                alert.setMessage("Alert: " + area.getName() + " is at FULL capacity!");
                alert.setStatus(AlertStatus.ACTIVE);
                alert.setTimestamp(LocalDateTime.now());
                alertService.saveAlert(alert);
            }
        } else {
            List<CrowdAlert> activeAlerts = alertService.getActiveAlertsByArea(area);
            for (CrowdAlert alert : activeAlerts) {
                alert.setStatus(AlertStatus.RESOLVED);
                alertService.saveAlert(alert);
            }
        }

        return savedReport;
    }

    public Optional<CrowdReport> getLatestReportForArea(FestivalArea area) {
        return crowdReportRepository.findFirstByAreaOrderByTimestampDesc(area);
    }

    public long countReports() {
        return crowdReportRepository.count();
    }
}
