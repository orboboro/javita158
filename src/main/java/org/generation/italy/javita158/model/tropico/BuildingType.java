package org.generation.italy.javita158.model.tropico;

import jakarta.persistence.*;

import java.math.BigDecimal;

/** Modello della tabella buildingtype, mappato per Hibernate. */
@Entity
@Table(name = "buildingtype", schema = "public")
public class BuildingType {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "category", length = 20)
    private String category;

    @Column(name = "construction_cost", nullable = false)
    private Integer constructionCost;

    @Column(name = "maintenance_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal maintenanceCost;

    @Column(name = "max_workers")
    private Integer maxWorkers = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_resource_id")
    private Resource inputResource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_resource_id")
    private Resource outputResource;

    @Column(name = "production_rate", precision = 5, scale = 2)
    private BigDecimal productionRate;

    public BuildingType() {
    }

    public BuildingType(
            Integer id,
            String name,
            String category,
            Integer constructionCost,
            BigDecimal maintenanceCost,
            Integer maxWorkers,
            Resource inputResource,
            Resource outputResource,
            BigDecimal productionRate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.constructionCost = constructionCost;
        this.maintenanceCost = maintenanceCost;
        this.maxWorkers = maxWorkers;
        this.inputResource = inputResource;
        this.outputResource = outputResource;
        this.productionRate = productionRate;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getConstructionCost() {
        return constructionCost;
    }

    public void setConstructionCost(Integer constructionCost) {
        this.constructionCost = constructionCost;
    }

    public BigDecimal getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(BigDecimal maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public Integer getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(Integer maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    public Resource getInputResource() {
        return inputResource;
    }

    public void setInputResource(Resource inputResource) {
        this.inputResource = inputResource;
    }

    public Resource getOutputResource() {
        return outputResource;
    }

    public void setOutputResource(Resource outputResource) {
        this.outputResource = outputResource;
    }

    public BigDecimal getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(BigDecimal productionRate) {
        this.productionRate = productionRate;
    }
}
