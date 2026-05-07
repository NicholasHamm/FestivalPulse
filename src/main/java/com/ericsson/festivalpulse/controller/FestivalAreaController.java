package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class FestivalAreaController {

    @Autowired
    private FestivalAreaRepository areaRepository;

    @PostMapping
    public ResponseEntity<FestivalArea> createArea(@RequestBody FestivalArea request) {
        FestivalArea savedArea = areaRepository.save(request);
        return ResponseEntity.ok(savedArea);
    }

    @GetMapping
    public ResponseEntity<List<FestivalArea>> getAllAreas() {
        return ResponseEntity.ok(areaRepository.findAll());
    }

}
