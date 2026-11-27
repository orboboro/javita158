package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.BuildingType;
import org.generation.italy.javita158.repositories.tropico.projections.BuildingCategorySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * LEZIONE 3: operatori IN/IS NULL, navigazione delle relazioni e aggregazioni.
 * BuildingType descrive un progetto (es. Distilleria di Rum); Building è un edificio
 * costruito. Nel DDL le categorie ammesse sono Extraction, Industry, Housing,
 * Service, Logistics, Government. Le ricerche per categoria sono case-sensitive.
 * Parametri non null; per IN passare una collezione non vuota per un uso portabile.
 */
public interface BuildingTypeRepository extends JpaRepository<BuildingType, Integer> {

    // 1. FILTRI SEMPLICI.

    /** Tutti i tipi di una categoria, per esempio "Industry". */
    List<BuildingType> findByCategory(String category);

    /** Stessa uguaglianza in JPQL; @Param rende esplicita l'associazione del parametro. */
    @Query("select bt from BuildingType bt where bt.category = :category")
    List<BuildingType> findByCategoryJpql(@Param("category") String category);

    /** IN accetta più valori: List.of("Industry", "Extraction") equivale a scegliere
     * l'una O l'altra categoria. Non passare una stringa con valori separati da virgole. */
    List<BuildingType> findByCategoryIn(Collection<String> categories);

    /** Tipi senza risorsa di ingresso: possono essere estrattivi, abitazioni o servizi.
     * L'assenza di input, da sola, non implica che il tipo produca una risorsa. */
    List<BuildingType> findByInputResourceIsNull();

    /** LessThan è strettamente minore: esclude il costo uguale al massimo. */
    List<BuildingType> findByConstructionCostLessThan(Integer maximum);

    // 2. ATTRAVERSARE UNA RELAZIONE: l'underscore delimita gli attributi del percorso.

    /** Segue outputResource.name. Il nome è quello della risorsa prodotta, non del tipo.
     * I tipi senza outputResource non corrispondono a un nome non null. */
    List<BuildingType> findByOutputResource_Name(String resourceName);

    /** JOIN percorre la relazione mappata: non riscriviamo la condizione sulla FK.
     * Un INNER JOIN esclude i tipi senza outputResource. */
    @Query("""
            select bt from BuildingType bt join bt.outputResource r
            where r.name = :resourceName
            """)
    List<BuildingType> findProducingResourceJpql(@Param("resourceName") String resourceName);

    // 3. PAGINAZIONE CON PIÙ FILTRI.

    /** Categoria esatta AND costo nell'intervallo inclusivo. Pageable controlla pagina
     * e ordinamento: usare constructionCost e id per risolvere i pareggi. */
    Page<BuildingType> findByCategoryAndConstructionCostBetween(
            String category, Integer minimum, Integer maximum, Pageable pageable);

    /** Equivalente JPQL. Il COUNT automatico conta gli stessi tipi della SELECT. */
    @Query("""
            select bt from BuildingType bt
            where bt.category = :category and bt.constructionCost between :minimum and :maximum
            """)
    Page<BuildingType> findAffordableTypesJpql(@Param("category") String category,
                                            @Param("minimum") Integer minimum,
                                            @Param("maximum") Integer maximum,
                                            Pageable pageable);

    // 4. SOTTOQUERY E REPORT: queste operazioni superano la derivazione dal nome.

    /** Confronta ogni costo con la media della PROPRIA categoria (sottoquery correlata).
     * Una categoria null non soddisfa il confronto di uguaglianza e viene esclusa. */
    @Query("""
            select bt from BuildingType bt
            where bt.constructionCost > (
                select avg(other.constructionCost) from BuildingType other
                where other.category = bt.category
            )
            order by bt.id
            """)
    List<BuildingType> findCostlierThanCategoryAverageJpql();

    /** Tipi mai costruiti: per ogni tipo cerchiamo se esiste almeno un Building.
     * Anche un edificio inattivo conta come costruito. Non serve una relazione inversa. */
    @Query("""
            select bt from BuildingType bt
            where not exists (select b.id from Building b where b.buildingType = bt)
            order by bt.id
            """)
    List<BuildingType> findNeverBuiltJpql();

    /** WHERE filtra le righe prima di raggruppare; HAVING filtra i gruppi già calcolati.
     * Qui escludiamo categorie null, poi conserviamo categorie con almeno N tipi.
     * Gli alias della SELECT corrispondono ai getter della proiezione. */
    @Query("""
            select bt.category as category, count(bt) as typeCount,
                   avg(bt.constructionCost) as averageConstructionCost,
                   max(bt.maintenanceCost) as maximumMaintenanceCost
            from BuildingType bt
            where bt.category is not null
            group by bt.category
            having count(bt) >= :minimumTypes
            order by bt.category
            """)
    List<BuildingCategorySummary> summarizeCategoriesJpql(@Param("minimumTypes") long minimumTypes);
}
