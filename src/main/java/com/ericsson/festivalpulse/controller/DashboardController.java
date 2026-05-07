package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.service.CrowdAlertService;
import com.ericsson.festivalpulse.service.CrowdReportService;
import com.ericsson.festivalpulse.service.FestivalAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private FestivalAreaService areaService;

    @Autowired
    private CrowdReportService reportService;

    @Autowired
    private CrowdAlertService alertService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(areaService.getDashboardSummary(reportService, alertService));
    }
}
