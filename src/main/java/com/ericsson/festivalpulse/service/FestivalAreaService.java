package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.dto.FestivalAreaStatus;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FestivalAreaService {
    private final FestivalAreaRepository festivalAreaRepository;
    private final CrowdReportService crowdReportService;

    FestivalAreaService(FestivalAreaRepository festivalAreaRepository, CrowdReportService crowdReportService) {
        this.festivalAreaRepository = festivalAreaRepository;
        this.crowdReportService = crowdReportService;
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

    public List<FestivalAreaStatus> getAreasStatus() {
        List<FestivalArea> areas = getAllAreas();
        return areas.stream().map(area -> {
            CrowdReport latest = crowdReportService.getLatestReportForArea(area).orElse(null);
            return new FestivalAreaStatus(
                area.getId(),
                area.getName(),
                area.getType(),
                latest != null ? latest.getCrowdLevel() : CrowdLevel.UNKNOWN,
                latest != null ? latest.getTimestamp() : null
            );
        }).collect(Collectors.toList());
    }
}
