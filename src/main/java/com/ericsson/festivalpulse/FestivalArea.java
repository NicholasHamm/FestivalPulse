package com.ericsson.festivalpulse;

public class FestivalArea {
    String name;
    String description;
    AreaType type;

    public FestivalArea(String name, String description, AreaType type) {
        this.name = name;
        this.description = description;
        this.type = type;
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
