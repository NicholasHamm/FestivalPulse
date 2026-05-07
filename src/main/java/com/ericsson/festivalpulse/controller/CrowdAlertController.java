package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class CrowdAlertController {

    @Autowired
    private CrowdAlertRepository alertRepository;

    @GetMapping
    public ResponseEntity<List<CrowdAlert>> getActiveAlerts() {
        return ResponseEntity.ok(alertRepository.findByStatus(AlertStatus.ACTIVE));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<CrowdAlert> resolveAlert(@PathVariable Long id) {
        CrowdAlert alert = alertRepository.findById(id).orElse(null);
        if (alert == null) {
            return ResponseEntity.notFound().build();
        }
        alert.setStatus(AlertStatus.RESOLVED);
        return ResponseEntity.ok(alertRepository.save(alert));
    }

}
