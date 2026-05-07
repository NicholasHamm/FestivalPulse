package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.dto.FestivalAreaStatus;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.models.Location;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import com.ericsson.festivalpulse.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FestivalAreaService {
    private final FestivalAreaRepository festivalAreaRepository;
    private final LocationRepository locationRepository;
    private final CrowdReportService crowdReportService;

    FestivalAreaService(FestivalAreaRepository festivalAreaRepository, LocationRepository locationRepository, CrowdReportService crowdReportService) {
        this.festivalAreaRepository = festivalAreaRepository;
        this.locationRepository = locationRepository;
        this.crowdReportService = crowdReportService;
    }

    public FestivalArea createArea(FestivalArea area) {
        if (festivalAreaRepository.existsFestivalAreasByName(area.getName())) {
            throw new IllegalArgumentException("Area already exists");
        }
        return festivalAreaRepository.save(area);
    }

    public FestivalArea getAreaById(Long id) {
        return festivalAreaRepository.findById(id).orElse(null);
    }

    public FestivalArea saveArea(FestivalArea area) {
        return festivalAreaRepository.save(area);
    }

    public void setAreaLocation(FestivalArea area, Long locationId) {
        if (locationId == null) {
            area.setLocation(null);
        } else {
            Location loc = locationRepository.findById(locationId).orElseThrow(() -> new IllegalArgumentException("Location not found"));
            area.setLocation(loc);
        }
    }

    public List<FestivalArea> getAllAreas() {
        return festivalAreaRepository.findAll();
    }

    public long countAreas() {
        return festivalAreaRepository.count();
    }

    public Map<String, Object> getDashboardSummary(CrowdReportService reportService, CrowdAlertService alertService) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAreas", countAreas());
        summary.put("totalReports", reportService.countReports());
        summary.put("activeAlerts", alertService.countActiveAlerts());
        return summary;
    }

    public List<FestivalAreaStatus> getAreasStatus() {
        return getAllAreas().stream().map(area -> {
            CrowdReport latest = crowdReportService.getLatestReportForArea(area).orElse(null);
            return new FestivalAreaStatus(
                area.getId(),
                area.getName(),
                area.getType(),
                latest != null ? latest.getCrowdLevel() : CrowdLevel.UNKNOWN,
                latest != null ? latest.getTimestamp() : null,
                area.getLocation() != null ? area.getLocation().getId() : null,
                area.getLocation() != null ? area.getLocation().getName() : null,
                area.getLocationX(),
                area.getLocationY()
            );
        }).collect(Collectors.toList());
    }
}
