package org.generation.italy.javita158.dtos.tropico;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.generation.italy.javita158.model.tropico.Faction;

public class FactionDTO {

    private Integer id;
    private String name;
    private String description;

    public FactionDTO(String description, String name, Integer id) {
        this.description = description;
        this.name = name;
        this.id = id;
    }

    public FactionDTO() {
    }

    public Faction toFaction() {
        return new Faction(this.id, this.name, this.description);
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
}
