package org.generation.italy.javita158.repositories.tropico.projections;

import java.math.BigDecimal;

/** Statistiche sui TIPI di edificio, non sul numero di edifici costruiti.
 * COUNT -> Long; AVG -> Double; MAX mantiene il tipo BigDecimal del costo. */
public interface BuildingCategorySummary {
    String getCategory();
    Long getTypeCount();
    Double getAverageConstructionCost();
    BigDecimal getMaximumMaintenanceCost();
}
