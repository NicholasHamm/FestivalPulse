package com.ericsson.festivalpulse.models;

import com.ericsson.festivalpulse.enums.AreaType;
import jakarta.persistence.*;

@Entity
public class FestivalArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private AreaType type;

    private Double locationX;
    private Double locationY;

    public FestivalArea() {}

    public FestivalArea(String name, String description, AreaType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AreaType getType() { return type; }
    public void setType(AreaType type) { this.type = type; }
    public Double getLocationX() { return locationX; }
    public void setLocationX(Double locationX) { this.locationX = locationX; }
    public Double getLocationY() { return locationY; }
    public void setLocationY(Double locationY) { this.locationY = locationY; }
}
