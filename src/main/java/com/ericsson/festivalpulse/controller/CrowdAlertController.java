package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.service.CrowdAlertService;
import com.ericsson.festivalpulse.service.CrowdReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class CrowdAlertController {

    @Autowired
    private CrowdAlertService alertService;

    @Autowired
    private CrowdReportService reportService;

    @GetMapping
    public ResponseEntity<List<CrowdAlert>> getActiveAlerts() {
        return ResponseEntity.ok(alertService.getAlertsByStatus(AlertStatus.ACTIVE));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<CrowdAlert> resolveAlert(@PathVariable Long id, @RequestParam CrowdLevel newLevel) {
        CrowdAlert alert = alertService.getAlertById(id).orElse(null);
        if (alert == null) {
            return ResponseEntity.notFound().build();
        }
        alert.setStatus(AlertStatus.RESOLVED);
        CrowdAlert savedAlert = alertService.saveAlert(alert);

        // Create a new report to reflect the updated crowd level
        CrowdReport report = new CrowdReport();
        report.setArea(alert.getArea());
        report.setCrowdLevel(newLevel);
        report.setTimestamp(LocalDateTime.now());
        report.setShortNote("Manually resolved alert");
        reportService.saveCrowdReport(report);

        return ResponseEntity.ok(savedAlert);
    }

}
