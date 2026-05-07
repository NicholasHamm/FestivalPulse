package com.ericsson.festivalpulse.dto;

import com.ericsson.festivalpulse.enums.AreaType;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import java.time.LocalDateTime;

public class FestivalAreaStatus {
    private Long id;
    private String name;
    private AreaType type;
    private CrowdLevel latestLevel;
    private LocalDateTime latestTime;

    public FestivalAreaStatus() {}

    public FestivalAreaStatus(Long id, String name, AreaType type, CrowdLevel latestLevel, LocalDateTime latestTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latestLevel = latestLevel;
        this.latestTime = latestTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AreaType getType() {
        return type;
    }

    public void setType(AreaType type) {
        this.type = type;
    }

    public CrowdLevel getLatestLevel() {
        return latestLevel;
    }

    public void setLatestLevel(CrowdLevel latestLevel) {
        this.latestLevel = latestLevel;
    }

    public LocalDateTime getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(LocalDateTime latestTime) {
        this.latestTime = latestTime;
    }
}
