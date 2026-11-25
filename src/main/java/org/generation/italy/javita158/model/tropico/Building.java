package org.generation.italy.javita158.model.tropico;

import jakarta.persistence.*;

/** Modello della tabella building, mappato per Hibernate. */
@Entity
@Table(name = "building", schema = "public")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_id_generator")
    @SequenceGenerator(name = "building_id_generator", sequenceName = "building_id_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_type_id", nullable = false)
    private BuildingType buildingType;

    @Column(name = "budget_level")
    private Integer budgetLevel = 3;

    @Column(name = "is_active")
    private Boolean active = true;

    public Building() {
    }

    public Building(
            Integer id,
            BuildingType buildingType,
            Integer budgetLevel,
            Boolean active) {
        this.id = id;
        this.buildingType = buildingType;
        this.budgetLevel = budgetLevel;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public Integer getBudgetLevel() {
        return budgetLevel;
    }

    public void setBudgetLevel(Integer budgetLevel) {
        this.budgetLevel = budgetLevel;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
