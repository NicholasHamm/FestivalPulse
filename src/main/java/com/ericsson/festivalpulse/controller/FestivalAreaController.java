package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.service.CrowdReportService;
import com.ericsson.festivalpulse.service.FestivalAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/areas")
public class FestivalAreaController {

    @Autowired
    private FestivalAreaService areaService;

    @Autowired
    private CrowdReportService reportService;

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
    public ResponseEntity<List<Map<String, Object>>> getAreasStatus() {
        List<FestivalArea> areas = areaService.getAllAreas();
        List<Map<String, Object>> statusList = areas.stream().map(area -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", area.getId());
            map.put("name", area.getName());
            map.put("type", area.getType());
            
            CrowdReport latest = reportService.getLatestReportForArea(area).orElse(null);
            map.put("latestLevel", latest != null ? latest.getCrowdLevel() : "UNKNOWN");
            map.put("latestTime", latest != null ? latest.getTimestamp() : null);
            return map;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(statusList);
    }

}
