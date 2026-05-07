package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.dto.FestivalAreaStatus;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.service.FestivalAreaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/areas")
public class FestivalAreaController {

    private final FestivalAreaService areaService;

    FestivalAreaController(FestivalAreaService areaService) {
        this.areaService = areaService;
    }

    @PostMapping
    public ResponseEntity<?> createArea(@RequestBody FestivalArea request) {
        try {
            return ResponseEntity.ok(areaService.createArea(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<FestivalArea>> getAllAreas() {
        return ResponseEntity.ok(areaService.getAllAreas());
    }

    @GetMapping("/status")
    public ResponseEntity<List<FestivalAreaStatus>> getAreasStatus() {
        return ResponseEntity.ok(areaService.getAreasStatus());
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        FestivalArea area = areaService.getAreaById(id);
        if (area == null) return ResponseEntity.notFound().build();
        if (body.containsKey("locationX")) area.setLocationX(((Number) body.get("locationX")).doubleValue());
        if (body.containsKey("locationY")) area.setLocationY(((Number) body.get("locationY")).doubleValue());
        return ResponseEntity.ok(areaService.saveArea(area));
    }
}
