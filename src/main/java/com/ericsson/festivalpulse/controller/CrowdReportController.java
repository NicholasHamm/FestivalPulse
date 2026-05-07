package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class CrowdReportController {

    @Autowired
    private CrowdReportRepository reportRepository;

    @Autowired
    private FestivalAreaRepository areaRepository;

    @Autowired
    private CrowdAlertRepository alertRepository;

    @PostMapping
    public ResponseEntity<?> submitReport(@RequestBody CrowdReport request) {
        if (request.getArea() == null || request.getArea().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Area ID is required");
        }

        FestivalArea area = areaRepository.findById(request.getArea().getId()).orElse(null);
        if (area == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Area not found");
        }

        request.setArea(area);
        if (request.getTimestamp() == null) {
            request.setTimestamp(LocalDateTime.now());
        }

        CrowdReport savedReport = reportRepository.save(request);

        if (savedReport.getCrowdLevel() == CrowdLevel.FULL) {
            List<CrowdAlert> activeAlerts = alertRepository.findByAreaAndStatus(area, AlertStatus.ACTIVE);
            if (activeAlerts.isEmpty()) {
                CrowdAlert alert = new CrowdAlert();
                alert.setArea(area);
                alert.setMessage("Alert: " + area.getName() + " is at FULL capacity!");
                alert.setStatus(AlertStatus.ACTIVE);
                alert.setTimestamp(LocalDateTime.now());
                alertRepository.save(alert);
            }
        }

        return ResponseEntity.ok(savedReport);
    }

    @GetMapping
    public ResponseEntity<List<CrowdReport>> getRecentReports() {
        return ResponseEntity.ok(reportRepository.findTop10ByOrderByTimestampDesc());
    }

}
