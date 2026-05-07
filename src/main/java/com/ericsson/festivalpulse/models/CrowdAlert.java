package com.ericsson.festivalpulse.models;

import com.ericsson.festivalpulse.enums.AlertStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CrowdAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FestivalArea area;

    private String message;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    private LocalDateTime timestamp;

    public CrowdAlert() {}

    public CrowdAlert(FestivalArea area, String message, AlertStatus status, LocalDateTime timestamp) {
        this.area = area;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
