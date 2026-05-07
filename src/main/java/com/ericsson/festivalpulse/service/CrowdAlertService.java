package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrowdAlertService {

    private final CrowdAlertRepository alertRepository;

    public CrowdAlertService(CrowdAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<CrowdAlert> getAlertsByStatus(AlertStatus status) {
        return alertRepository.findByStatus(status);
    }

    public List<CrowdAlert> getActiveAlertsByArea(FestivalArea area) {
        return alertRepository.findByAreaAndStatus(area, AlertStatus.ACTIVE);
    }

    public Optional<CrowdAlert> getAlertById(Long id) {
        return alertRepository.findById(id);
    }

    public CrowdAlert saveAlert(CrowdAlert alert) {
        return alertRepository.save(alert);
    }

    public CrowdAlert resolveAlert(Long id, CrowdLevel newLevel, CrowdReportService reportService) {
        CrowdAlert alert = getAlertById(id).orElse(null);
        if (alert == null) {
            return null;
        }
        alert.setStatus(AlertStatus.RESOLVED);
        CrowdAlert savedAlert = saveAlert(alert);

        // Create a new report to reflect the updated crowd level
        CrowdReport report = new CrowdReport();
        report.setArea(alert.getArea());
        report.setCrowdLevel(newLevel);
        report.setTimestamp(java.time.LocalDateTime.now());
        report.setShortNote("Manually resolved alert");
        reportService.saveCrowdReport(report);

        return savedAlert;
    }

    public long countActiveAlerts() {
        return alertRepository.findByStatus(AlertStatus.ACTIVE).size();
    }
}
