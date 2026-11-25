package org.generation.italy.javita158.dtos.tropico;

import org.generation.italy.javita158.model.tropico.Resource;

import java.math.BigDecimal;

public class ResourceDTO {
    private Integer id;
    private String name;
    private BigDecimal baseMarketPrice;
    private String processed;
    public static final String PROCESSED = "Processed";
    public static final String NOT_PROCESSED = "Not Processed";


    public ResourceDTO() {} //serve per ricevere i DTO in entrata: Spring usa prima il costruttore di default

    public ResourceDTO(Resource r) {
        this.id = r.getId();
        this.name = r.getName();
        this.baseMarketPrice = r.getBaseMarketPrice();
        this.processed = r.getProcessed() ? PROCESSED : NOT_PROCESSED;
    }

    public Resource toResource() {
        return new Resource(
                id,
                name,
                baseMarketPrice,
                processed.equals(PROCESSED));
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

    public BigDecimal getBaseMarketPrice() {
        return baseMarketPrice;
    }

    public void setBaseMarketPrice(BigDecimal baseMarketPrice) {
        this.baseMarketPrice = baseMarketPrice;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }
}
