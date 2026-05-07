package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.dto.FestivalAreaStatus;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.service.FestivalAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class FestivalAreaController {

    @Autowired
    private FestivalAreaService areaService;

    @PostMapping
    public ResponseEntity<FestivalArea> createArea(@RequestBody FestivalArea request) {
        FestivalArea savedArea = areaService.createArea(request);
        return ResponseEntity.ok(savedArea);
    }

    @GetMapping
    public ResponseEntity<List<FestivalArea>> getAllAreas() {
        return ResponseEntity.ok(areaService.getAllAreas());
    }

    @GetMapping("/status")
    public ResponseEntity<List<FestivalAreaStatus>> getAreasStatus() {
        return ResponseEntity.ok(areaService.getAreasStatus());
    }
}
