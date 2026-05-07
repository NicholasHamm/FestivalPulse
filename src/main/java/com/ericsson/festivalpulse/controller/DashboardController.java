package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private FestivalAreaRepository areaRepository;

    @Autowired
    private CrowdReportRepository reportRepository;

    @Autowired
    private CrowdAlertRepository alertRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAreas", areaRepository.count());
        summary.put("totalReports", reportRepository.count());
        summary.put("activeAlerts", alertRepository.findByStatus(AlertStatus.ACTIVE).size());
        return ResponseEntity.ok(summary);
    }
}
