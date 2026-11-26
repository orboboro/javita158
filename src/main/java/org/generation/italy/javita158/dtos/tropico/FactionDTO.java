package org.generation.italy.javita158.dtos.tropico;

import org.generation.italy.javita158.model.tropico.Faction;

public class FactionDTO {
    private Integer id;
    private String name;
    private String description;

    public FactionDTO() {
    }

    public FactionDTO(Faction f) {
        this.id = f.getId();
        this.name = f.getName();
        this.description = f.getDescription();
    }

    public Faction toFaction() {
        return new Faction(id, name, description);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, name:%s, description:%s", id, name, description);
    }
}
