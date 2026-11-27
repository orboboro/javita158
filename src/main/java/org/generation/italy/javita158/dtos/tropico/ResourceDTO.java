package org.generation.italy.javita158.dtos.tropico;

import org.generation.italy.javita158.model.tropico.Resource;

import java.math.BigDecimal;

public class ResourceDTO {
    private Integer id;
    private String name;
    private BigDecimal baseMarketPrice;
    private String processed;

    public final static String PROCESSED = "PROCESSED";
    public final static String NOT_PROCESSED = "NOT_PROCESSED";

    public ResourceDTO(){
    }

    public ResourceDTO(Resource r) {
        this.id = r.getId();
        this.name = r.getName();
        this.baseMarketPrice = r.getBaseMarketPrice();
        this.processed = r.getProcessed() ? PROCESSED : NOT_PROCESSED;
    }

    public Resource toResource(){
        return new Resource(this.id, this.name, this.baseMarketPrice, this.processed.equals(PROCESSED));
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBaseMarketPrice() {
        return baseMarketPrice;
    }

    public String getProcessed() {
        return processed;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBaseMarketPrice(BigDecimal baseMarketPrice) {
        this.baseMarketPrice = baseMarketPrice;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }
}
