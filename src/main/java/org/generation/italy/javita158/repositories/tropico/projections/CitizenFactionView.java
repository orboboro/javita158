package org.generation.italy.javita158.repositories.tropico.projections;

/** Proiezione scalare: selezioniamo solo le informazioni necessarie per la lista.
 * factionName può essere null: il LEFT JOIN conserva i cittadini senza fazione. */
public interface CitizenFactionView {
    Integer getCitizenId();
    String getFirstName();
    String getFactionName();
}
