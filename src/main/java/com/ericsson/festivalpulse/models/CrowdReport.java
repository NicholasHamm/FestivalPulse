package com.ericsson.festivalpulse.models;

import com.ericsson.festivalpulse.enums.CrowdLevel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CrowdReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FestivalArea area;

    @Enumerated(EnumType.STRING)
    private CrowdLevel crowdLevel;

    private LocalDateTime timestamp;
    private String shortNote;

    public CrowdReport() {}

    public CrowdReport(FestivalArea area, CrowdLevel crowdLevel, LocalDateTime timestamp) {
        this.area = area;
        this.crowdLevel = crowdLevel;
        this.timestamp = timestamp;
        this.shortNote = "";
    }

    public CrowdReport(FestivalArea area, CrowdLevel crowdLevel, LocalDateTime timestamp, String shortNote) {
        this.area = area;
        this.crowdLevel = crowdLevel;
        this.timestamp = timestamp;
        this.shortNote = shortNote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FestivalArea getArea() {
        return area;
    }

    public void setArea(FestivalArea area) {
        this.area = area;
    }

    public CrowdLevel getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(CrowdLevel crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
