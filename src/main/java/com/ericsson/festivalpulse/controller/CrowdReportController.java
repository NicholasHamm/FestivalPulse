package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.models.CrowdReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class CrowdReportController {

    @PostMapping
    public ResponseEntity<CrowdReport> submitReport(@RequestBody CrowdReport request) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<CrowdReport>> getRecentReports() {
        return null;
    }

}
