package org.generation.italy.javita158.model.tropico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/** Modello della tabella resource, mappato per Hibernate. */
@Entity
@Table(name = "resource", schema = "public")
public class Resource {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "base_market_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseMarketPrice;

    @Column(name = "is_processed")
    private Boolean processed = false;

    public Resource() {
    }

    public Resource(
            Integer id,
            String name,
            BigDecimal baseMarketPrice,
            Boolean processed) {
        this.id = id;
        this.name = name;
        this.baseMarketPrice = baseMarketPrice;
        this.processed = processed;
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

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }
}

