package com.ericsson.festivalpulse.repository;

import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.models.CrowdReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrowdReportRepository extends JpaRepository<CrowdReport, Long> {
    List<CrowdReport> findTop10ByOrderByTimestampDesc();
    Optional<CrowdReport> findFirstByAreaOrderByTimestampDesc(FestivalArea area);
}
