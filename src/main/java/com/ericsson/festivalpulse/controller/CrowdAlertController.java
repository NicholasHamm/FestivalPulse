package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.models.CrowdAlert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class CrowdAlertController {

    @GetMapping
    public ResponseEntity<List<CrowdAlert>> getActiveAlerts() {
        return null;
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<CrowdAlert> resolveAlert(@PathVariable String id) {
        return null;
    }

}
