package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.service.CrowdAlertService;
import com.ericsson.festivalpulse.service.CrowdReportService;
import com.ericsson.festivalpulse.service.FestivalAreaService;
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
    private CrowdReportService reportService;

    @Autowired
    private FestivalAreaService areaService;

    @Autowired
    private CrowdAlertService alertService;

    @PostMapping
    public ResponseEntity<?> submitReport(@RequestBody CrowdReport request) {
        try {
            CrowdReport savedReport = reportService.submitReport(request, areaService, alertService);
            return ResponseEntity.ok(savedReport);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CrowdReport>> getRecentReports() {
        return ResponseEntity.ok(reportService.getRecentCrowdReports());
    }

}