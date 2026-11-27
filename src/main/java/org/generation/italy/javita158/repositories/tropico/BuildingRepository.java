package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * LEZIONE 4: dagli attributi dell'edificio alle relazioni, fino al piano di caricamento.
 * Building -> buildingType è obbligatoria; Citizen -> jobBuilding è facoltativa.
 * Un edificio non contiene una lista di lavoratori nel modello Java: per contarli
 * o cercarli partiamo da Citizen in una sottoquery. Parametri di ricerca non null.
 */
public interface BuildingRepository extends JpaRepository<Building, Integer> {

    // 1. FILTRI DERIVATI.

    /** Solo active = true; un Boolean null non è equivalente a true né a false. */
    List<Building> findByActiveTrue();

    /** Budget compreso fra i due estremi inclusi. Il DDL ammette valori da 1 a 5. */
    List<Building> findByBudgetLevelBetween(Integer minimum, Integer maximum);

    /** Attraversa buildingType.category: si filtrano edifici, non progetti di edificio. */
    List<Building> findByBuildingType_Category(String category);

    /** Equivalente JPQL con JOIN esplicito sulla relazione mappata. Non occorre
     * scrivere "ON b.building_type_id = ...": JPA conosce già la JoinColumn. */
    @Query("""
            select b from Building b join b.buildingType bt
            where bt.category = :category
            """)
    List<Building> findInCategoryJpql(@Param("category") String category);

    // 2. PAGINA SU UNA RELAZIONE: stesso risultato con nome derivato e JPQL.

    /** Edifici attivi di una categoria. Per esempio PageRequest.of(0, 2, Sort.by("id")).
     * La relazione ManyToOne non moltiplica le righe: ogni edificio ha un solo tipo. */
    Page<Building> findByActiveTrueAndBuildingType_Category(String category, Pageable pageable);

    /** Stesso filtro e stesso Pageable del metodo precedente. JOIN serve a filtrare;
     * da solo non promette di inizializzare l'associazione lazy nel risultato. */
    @Query("""
            select b from Building b join b.buildingType bt
            where b.active = true and bt.category = :category
            """)
    Page<Building> findActiveInCategoryJpql(@Param("category") String category, Pageable pageable);

    // 3. SOTTOQUERY CORRELATE: assenza, conteggio ed esistenza di lavoratori.

    /** Edifici attivi cui nessun cittadino è assegnato come lavoratore.
     * Include anche le abitazioni senza lavoratori: "senza lavoratori" non significa
     * automaticamente "da assumere". Il metodo non filtra categoria né capacità. */
    @Query("""
            select b from Building b
            where b.active = true
              and not exists (select c.id from Citizen c where c.jobBuilding = b)
            order by b.id
            """)
    List<Building> findActiveWithoutWorkersJpql();

    /** Edifici con più lavoratori della capacità dichiarata dal tipo. La sottoquery
     * restituisce un COUNT per ogni b. A parità di conteggio e capacità NON c'è eccesso;
     * capacità null significa sconosciuta e non soddisfa il confronto. */
    @Query("""
            select b from Building b
            where (select count(c) from Citizen c where c.jobBuilding = b) > b.buildingType.maxWorkers
            order by b.id
            """)
    List<Building> findOverstaffedJpql();

    /** Almeno un lavoratore sotto la soglia di felicità. EXISTS evita duplicati:
     * dieci lavoratori infelici nello stesso edificio restituiscono un solo edificio. */
    @Query("""
            select b from Building b
            where exists (
                select c.id from Citizen c
                where c.jobBuilding = b and c.happinessTotal < :threshold
            )
            order by b.id
            """)
    List<Building> findWithUnhappyWorkersJpql(@Param("threshold") Integer threshold);

    // 4. FETCH: come caricare i risultati, oltre a scegliere quali risultati ottenere.

    /** JOIN FETCH inizializza anche buildingType con la stessa SELECT. Senza FETCH,
     * leggere il nome del tipo per ciascun edificio può causare SELECT aggiuntive
     * (problema N+1, a seconda della cache). Non risolviamo rendendo tutto EAGER.
     * La relazione obbligatoria permette INNER JOIN FETCH senza perdere edifici. */
    @Query("select b from Building b  where b.active = true order by b.id")
    List<Building> findActiveWithTypeJpql();
}
