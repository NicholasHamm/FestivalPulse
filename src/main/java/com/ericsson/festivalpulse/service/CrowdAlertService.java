package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrowdAlertService {

    private final CrowdAlertRepository alertRepository;

    public CrowdAlertService(CrowdAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<CrowdAlert> getAlertsByStatus(AlertStatus status) {
        return alertRepository.findByStatus(status);
    }

    public List<CrowdAlert> getActiveAlertsByArea(FestivalArea area) {
        return alertRepository.findByAreaAndStatus(area, AlertStatus.ACTIVE);
    }

    public Optional<CrowdAlert> getAlertById(Long id) {
        return alertRepository.findById(id);
    }

    public CrowdAlert saveAlert(CrowdAlert alert) {
        return alertRepository.save(alert);
    }

    public long countActiveAlerts() {
        return alertRepository.findByStatus(AlertStatus.ACTIVE).size();
    }
}
