package com.ericsson.festivalpulse.models;

import com.ericsson.festivalpulse.enums.CrowdLevel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public record CrowdReport(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,

        @ManyToOne
        FestivalArea area,

        @Enumerated(EnumType.STRING)
        CrowdLevel crowdLevel,

        LocalDateTime timestamp,
        String shortNote
) {
    public CrowdReport(FestivalArea area, CrowdLevel crowdLevel, LocalDateTime timestamp) {
        this(null, area, crowdLevel, timestamp, "");
    }

    public CrowdReport(FestivalArea area, CrowdLevel crowdLevel, LocalDateTime timestamp, String shortNote) {
        this(null, area, crowdLevel, timestamp, shortNote);
    }
}