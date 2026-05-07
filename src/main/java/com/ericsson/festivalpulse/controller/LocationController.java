package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.models.Location;
import com.ericsson.festivalpulse.repository.LocationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationRepository locationRepository;

    LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location) {
        return ResponseEntity.ok(locationRepository.save(location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
