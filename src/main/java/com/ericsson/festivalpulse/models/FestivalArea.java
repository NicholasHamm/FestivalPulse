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

    public FestivalArea() {}

    public FestivalArea(String name, String description, AreaType type) {
        this.name = name;
        this.description = description;
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public AreaType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(AreaType type) {
        this.type = type;
    }
}
