package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Citizen;
import org.generation.italy.javita158.repositories.tropico.projections.CitizenFactionView;
import org.generation.italy.javita158.repositories.tropico.projections.FactionSupportSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * LEZIONE 5: combinare quanto imparato per ricerche e report sui cittadini.
 * Le relazioni lavoro, casa e fazione sono facoltative: ragionare sempre sui null.
 * Le query derivate possono attraversare anche più relazioni; non serve JPQL solo
 * perché c'è un JOIN. Usiamo JPQL quando migliora la leggibilità o servono operazioni
 * che il nome del metodo non descrive, come sottoquery e GROUP BY/HAVING.
 * I parametri sono non null; IN richiede qui una collezione non vuota.
 */
public interface CitizenRepository extends JpaRepository<Citizen, Integer> {

    // 1. FILTRI DI BASE: intervalli, testo, assenza di relazioni, più valori.

    /** Età compresa fra gli estremi inclusi. Un'età null non soddisfa BETWEEN. */
    List<Citizen> findByAgeBetween(Integer minimum, Integer maximum);

    /** Sottostringa del cognome, ignorando il caso; non implica unicità del risultato. */
    List<Citizen> findByLastNameContainingIgnoreCase(String text);

    /** Senza lavoro significa jobBuilding IS NULL, non salary = 0:
     * le due condizioni esprimono informazioni diverse. */
    List<Citizen> findByJobBuildingIsNull();

    /** Senza casa: IS NULL si applica alla relazione, senza caricare l'edificio. */
    List<Citizen> findByHomeBuildingIsNull();

    /** IN seleziona uno dei livelli richiesti. Valori del DDL: Illiterate,
     * GradeSchool, HighSchool, College. Passare una Collection, non una stringa CSV. */
    List<Citizen> findByEducationLevelIn(Collection<String> levels);

    // 2. DUE COPPIE: stessa domanda, sintassi diversa.

    /** Devono essere vere ENTRAMBE le condizioni: ribelle e felicità sotto soglia. */
    List<Citizen> findByRebelTrueAndHappinessTotalLessThan(Integer threshold);

    /** Versione JPQL della stessa AND; il confronto è strettamente minore. */
    @Query("select c from Citizen c where c.rebel = true and c.happinessTotal < :threshold")
    List<Citizen> findUnhappyRebelsJpql(@Param("threshold") Integer threshold);

    /** supportedFaction.name è un percorso di attributi. L'underscore rende esplicita
     * la separazione; non rappresenta l'underscore di una colonna SQL. */
    List<Citizen> findBySupportedFaction_Name(String factionName);

    /** INNER JOIN: i cittadini senza fazione sono esclusi. Il nome richiesto è esatto. */
    @Query("""
            select c from Citizen c join c.supportedFaction f
            where f.name = :factionName
            """)
    List<Citizen> findSupportersJpql(@Param("factionName") String factionName);

    // 3. PAGINAZIONE ATTRAVERSO PIÙ RELAZIONI.

    /** Segue c.jobBuilding.buildingType.category. Gli unemployed non corrispondono.
     * Esempio di ordine stabile: PageRequest.of(0, 10, Sort.by("lastName").and(Sort.by("id"))). */
    Page<Citizen> findByJobBuilding_BuildingType_Category(String category, Pageable pageable);

    /** La SELECT carica solo la pagina; countQuery calcola il TOTALE dei cittadini
     * che soddisfano gli stessi JOIN e filtri. Qui il COUNT esplicito è didattico:
     * Spring potrebbe ricavarlo. Non contiene ORDER BY né FETCH, inutili al conteggio.
     * I JOIN ManyToOne non duplicano c: COUNT(c) è sufficiente. Con JOIN a collezioni
     * bisognerebbe ragionare su DISTINCT sia nella SELECT sia nel COUNT. */
    @Query(value = """
            select c from Citizen c
            join c.jobBuilding b join b.buildingType bt
            where bt.category = :category
            """, countQuery = """
            select count(c) from Citizen c
            join c.jobBuilding b join b.buildingType bt
            where bt.category = :category
            """)
    Page<Citizen> findWorkersInCategoryJpql(@Param("category") String category, Pageable pageable);

    // 4. JPQL ESPRESSIVO: parentesi e catene di JOIN.

    /** Adulti che sono ribelli O infelici. Le parentesi fanno applicare il requisito
     * dell'età a entrambi i casi. Senza parentesi, AND avrebbe precedenza su OR e
     * includeremmo anche minorenni infelici. Un adulto ribelle con felicità null passa.
     * Il nome derivato non offre parentesi: JPQL rende immediata la logica richiesta. */
    @Query("""
            select c from Citizen c
            where c.age >= :adultAge and (c.rebel = true or c.happinessTotal < :threshold)
            order by c.id
            """)
    List<Citizen> findAdultsNeedingAttentionJpql(@Param("adultAge") Integer adultAge,
                                               @Param("threshold") Integer threshold);

    /** Lavoratori di edifici attivi che producono una risorsa con questo nome.
     * I JOIN seguono quattro entità; escludono disoccupati e tipi senza output.
     * Una query derivata sarebbe possibile ma il nome diventerebbe molto lungo:
     * JPQL qui è una scelta di leggibilità, non una necessità tecnica. */
    @Query("""
            select c from Citizen c
            join c.jobBuilding b join b.buildingType bt join bt.outputResource r
            where b.active = true and r.name = :resourceName
            order by c.id
            """)
    List<Citizen> findWorkersProducingJpql(@Param("resourceName") String resourceName);

    /** LEFT JOIN conserva anche chi non sostiene una fazione: factionName sarà null.
     * La proiezione carica solo tre valori. Spostare un filtro su f nel WHERE potrebbe
     * eliminare le righe senza fazione, vanificando l'effetto del LEFT JOIN. */
    @Query("""
            select c.id as citizenId, c.firstName as firstName, f.name as factionName
            from Citizen c left join c.supportedFaction f
            order by c.id
            """)
    List<CitizenFactionView> findCitizenFactionViewsJpql();

    // 5. OLTRE I NOMI DERIVATI: medie, sottoquery correlate e aggregazioni.

    /** Sopra la media di TUTTI gli stipendi non null, inclusi eventuali zeri.
     * Un salario null non entra nella media e non supera la media. */
    @Query("""
            select c from Citizen c
            where c.salary > (select avg(other.salary) from Citizen other)
            order by c.id
            """)
    List<Citizen> findEarningAboveAverageJpql();

    /** La media considera solo i colleghi nello STESSO edificio. Il riferimento a c
     * rende la sottoquery correlata. Chi non lavora è escluso esplicitamente.
     * Con un solo lavoratore il salario coincide con la media e non viene selezionato. */
    @Query("""
            select c from Citizen c
            where c.jobBuilding is not null and c.salary > (
                select avg(other.salary) from Citizen other where other.jobBuilding = c.jobBuilding
            )
            order by c.id
            """)
    List<Citizen> findEarningAboveWorkplaceAverageJpql();

    /** Una riga per fazione con almeno N sostenitori. INNER JOIN esclude cittadini
     * senza fazione e fazioni senza sostenitori. GROUP BY include id e nome; HAVING
     * filtra il conteggio del gruppo, non i singoli cittadini. COUNT(c) include anche
     * chi ha felicità null; AVG(c.happinessTotal) ignora quei null.
     * Il risultato è un report (proiezione), non una lista di entità da modificare. */
    @Query("""
            select f.name as factionName, count(c) as supporterCount,
                   avg(c.happinessTotal) as averageHappiness
            from Citizen c join c.supportedFaction f
            group by f.id, f.name
            having count(c) >= :minimumSupporters
            order by count(c) desc, f.name asc
            """)
    List<FactionSupportSummary> summarizeFactionSupportJpql(
            @Param("minimumSupporters") long minimumSupporters);
}
