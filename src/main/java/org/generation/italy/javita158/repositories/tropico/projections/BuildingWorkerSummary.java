package org.generation.italy.javita158.repositories.tropico.projections;

/**
 * DTO immutabile: una riga del report dei lavoratori per edificio, non un'entità.
 * JPQL SELECT NEW usa il nome completo della classe e chiama questo costruttore.
 * Ordine e tipi degli argomenti devono corrispondere: id -> Integer, COUNT -> Long.
 * Non servono @Entity, setter o un costruttore vuoto.
 *
 * Le altre proiezioni del package sono interfacce: Spring Data crea a runtime un
 * proxy che le implementa, collegando gli alias della query ai getter
 * (es. "supporterCount" -> getSupporterCount()). Non genera un file Java nel progetto.
 * Questa funzionalità esiste da Spring Data JPA 1.10 (2016).
 * Qui invece Hibernate costruisce un'istanza della classe che abbiamo scritto.
 */
public final class BuildingWorkerSummary {

    private final Integer buildingId;
    private final Long workerCount;

    public BuildingWorkerSummary(Integer buildingId, Long workerCount) {
        this.buildingId = buildingId;
        this.workerCount = workerCount;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public Long getWorkerCount() {
        return workerCount;
    }
}
