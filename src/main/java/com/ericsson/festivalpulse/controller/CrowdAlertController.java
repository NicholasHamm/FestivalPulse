package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class CrowdAlertController {

    @Autowired
    private CrowdAlertRepository alertRepository;

    @Autowired
    private CrowdReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<List<CrowdAlert>> getActiveAlerts() {
        return ResponseEntity.ok(alertRepository.findByStatus(AlertStatus.ACTIVE));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<CrowdAlert> resolveAlert(@PathVariable Long id, @RequestParam CrowdLevel newLevel) {
        CrowdAlert alert = alertRepository.findById(id).orElse(null);
        if (alert == null) {
            return ResponseEntity.notFound().build();
        }
        alert.setStatus(AlertStatus.RESOLVED);
        CrowdAlert savedAlert = alertRepository.save(alert);

        // Create a new report to reflect the updated crowd level
        CrowdReport report = new CrowdReport();
        report.setArea(alert.getArea());
        report.setCrowdLevel(newLevel);
        report.setTimestamp(LocalDateTime.now());
        report.setShortNote("Manually resolved alert");
        reportRepository.save(report);

        return ResponseEntity.ok(savedAlert);
    }

}
