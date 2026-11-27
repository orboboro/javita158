package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Faction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * LEZIONE 2: testo, pagine ed esistenza di relazioni.
 * Nel modello Citizen conosce supportedFaction, ma Faction NON ha una collezione
 * citizens. Non possiamo inventare "f.citizens": interroghiamo Citizen nelle
 * sottoquery. Confrontare c.supportedFaction = f significa confrontare l'identità
 * delle entità; Hibernate traduce il confronto usando la chiave esterna.
 * Gli argomenti delle ricerche sono non null; nessun ordinamento è implicito.
 */
public interface FactionRepository extends JpaRepository<Faction, Integer> {

    // 1. RICERCHE DERIVATE E PRIMA COPPIA EQUIVALENTE.

    /** Il nome è UNIQUE nel DDL: Optional rappresenta l'eventuale assenza. */
    Optional<Faction> findByName(String name);

    /** Equivalente JPQL: Faction è l'entità, non il nome SQL della tabella. */
    @Query("select f from Faction f where f.name = :name")
    Optional<Faction> findByNameJpql(@Param("name") String name);

    /** Ricerca parziale senza distinguere maiuscole e minuscole; può trovare più fazioni. */
    List<Faction> findByNameContainingIgnoreCase(String text);

    /** Cerca una parola/frammento nella descrizione. Le descrizioni null non corrispondono
     * al filtro; ContainingIgnoreCase non significa ricerca linguistica per parole intere. */
    List<Faction> findByDescriptionContainingIgnoreCase(String text);

    // 2. PAGINAZIONE: la condizione è fissa, l'ordinamento arriva dal chiamante.

    /** Fazioni con una descrizione non null. Anche la stringa vuota soddisfa IS NOT NULL.
     * Passare, per esempio, PageRequest.of(0, 3, Sort.by("name").and(Sort.by("id"))). */
    Page<Faction> findByDescriptionIsNotNull(Pageable pageable);

    /** Stessa condizione, contenuto e totale del metodo derivato se il Pageable è uguale. */
    @Query("select f from Faction f where f.description is not null")
    Page<Faction> findDescribedFactionsJpql(Pageable pageable);

    // 3. SOTTOQUERY CORRELATE: non esprimibili semplicemente nel nome del metodo.

    /** Fazioni con almeno un sostenitore ribelle. EXISTS non moltiplica le righe
     * esterne: due ribelli della stessa fazione producono UNA fazione nel risultato. */
    @Query("""
            select f from Faction f
            where exists (
                select c.id from Citizen c where c.supportedFaction = f and c.rebel = true
            )
            order by f.id
            """)
    List<Faction> findWithRebelSupportersJpql();

    /** Fazioni senza sostenitori. Un cittadino senza fazione non appartiene ad alcuna
     * fazione e non impedisce che NOT EXISTS sia vero. */
    @Query("""
            select f from Faction f
            where not exists (select c.id from Citizen c where c.supportedFaction = f)
            order by f.id
            """)
    List<Faction> findWithoutSupportersJpql();

    /** La media viene calcolata separatamente per ogni fazione, grazie al riferimento f.
     * AVG ignora felicità null; senza valori la media è null e il confronto non è vero.
     * Le fazioni vuote quindi NON vengono considerate "infelici". */
    @Query("""
            select f from Faction f
            where (select avg(c.happinessTotal) from Citizen c where c.supportedFaction = f)
                  < :threshold
            order by f.id
            """)
    List<Faction> findWithAverageHappinessBelowJpql(@Param("threshold") double threshold);
}
