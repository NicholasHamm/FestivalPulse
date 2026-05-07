package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
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
    private FestivalAreaRepository areaRepository;

    @Autowired
    private CrowdReportRepository reportRepository;

    @PostMapping
    public ResponseEntity<FestivalArea> createArea(@RequestBody FestivalArea request) {
        FestivalArea savedArea = areaRepository.save(request);
        return ResponseEntity.ok(savedArea);
    }

    @GetMapping
    public ResponseEntity<List<FestivalArea>> getAllAreas() {
        return ResponseEntity.ok(areaRepository.findAll());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Map<String, Object>>> getAreasStatus() {
        List<FestivalArea> areas = areaRepository.findAll();
        List<Map<String, Object>> statusList = areas.stream().map(area -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", area.getId());
            map.put("name", area.getName());
            map.put("type", area.getType());
            
            CrowdReport latest = reportRepository.findFirstByAreaOrderByTimestampDesc(area).orElse(null);
            map.put("latestLevel", latest != null ? latest.getCrowdLevel() : "UNKNOWN");
            map.put("latestTime", latest != null ? latest.getTimestamp() : null);
            return map;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(statusList);
    }

}
