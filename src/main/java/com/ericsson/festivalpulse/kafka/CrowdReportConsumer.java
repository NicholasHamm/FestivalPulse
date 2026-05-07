package com.ericsson.festivalpulse.kafka;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import com.ericsson.festivalpulse.service.FestivalAreaService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CrowdReportConsumer {

    private final FestivalAreaService areaService;
    private final CrowdAlertRepository alertRepository;

    public CrowdReportConsumer(FestivalAreaService areaService, CrowdAlertRepository alertRepository) {
        this.areaService = areaService;
        this.alertRepository = alertRepository;
    }

    @KafkaListener(topics = "crowd-reports", groupId = "festival-pulse")
    public void onCrowdReport(CrowdReportEvent event) {
        if (event.crowdLevel() != CrowdLevel.FULL) return;

        FestivalArea area = areaService.getAreaById(event.areaId());
        if (area == null) return;

        boolean alreadyActive = !alertRepository.findByAreaAndStatus(area, AlertStatus.ACTIVE).isEmpty();
        if (alreadyActive) return;

        CrowdAlert alert = new CrowdAlert();
        alert.setArea(area);
        alert.setMessage("Alert: " + area.getName() + " is at FULL capacity!");
        alert.setStatus(AlertStatus.ACTIVE);
        alert.setTimestamp(LocalDateTime.now());
        alertRepository.save(alert);
    }
}
