package com.ericsson.festivalpulse.repository;

import com.ericsson.festivalpulse.models.CrowdReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrowdReportRepository extends JpaRepository<CrowdReport, Long> {
    List<CrowdReport> findTop10ByOrderByTimestampDesc();
}
