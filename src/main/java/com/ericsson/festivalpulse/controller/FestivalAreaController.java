package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.models.FestivalArea;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class FestivalAreaController {

    @PostMapping
    public ResponseEntity<FestivalArea> createArea(@RequestBody FestivalArea request) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<FestivalArea>> getAllAreas() {
        return null;
    }

}
