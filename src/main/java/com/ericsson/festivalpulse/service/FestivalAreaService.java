package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalAreaService {
    private final FestivalAreaRepository festivalAreaRepository;

    FestivalAreaService(FestivalAreaRepository festivalAreaRepository) {
        this.festivalAreaRepository = festivalAreaRepository;
    }

    public FestivalArea createArea(FestivalArea area) {
        return festivalAreaRepository.save(area);
    }
    public FestivalArea getAreaById(Long id) {
        return festivalAreaRepository.findById(id).orElse(null);
    }
    public List<FestivalArea> getAllAreas() {
        return festivalAreaRepository.findAll();
    }

    public long countAreas() {
        return festivalAreaRepository.count();
    }
}
