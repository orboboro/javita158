package org.generation.italy.javita158.repositories.tropico.projections;

/** Riga di un report, non un'entità: non ha @Entity e non si salva con save.
 * Spring collega gli alias processed/resourceCount/averagePrice ai getter. */
public interface ResourcePriceSummary {
    Boolean getProcessed();
    Long getResourceCount();
    Double getAveragePrice();
}
