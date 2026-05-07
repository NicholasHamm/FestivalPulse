package com.ericsson.festivalpulse.repository;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.FestivalArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrowdAlertRepository extends JpaRepository<CrowdAlert, Long> {
    List<CrowdAlert> findByStatus(AlertStatus status);
    List<CrowdAlert> findByAreaAndStatus(FestivalArea area, AlertStatus status);
}
