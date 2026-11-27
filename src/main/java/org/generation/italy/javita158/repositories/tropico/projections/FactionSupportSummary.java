package org.generation.italy.javita158.repositories.tropico.projections;

/** Il risultato aggregato ha un nome, un conteggio e una media, non un Citizen.
 * Long e Double sono i tipi definiti da JPQL per COUNT e AVG. AVG ignora i null. */
public interface FactionSupportSummary {
    String getFactionName();
    Long getSupporterCount();
    Double getAverageHappiness();
}
