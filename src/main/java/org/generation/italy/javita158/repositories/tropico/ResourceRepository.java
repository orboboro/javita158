package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.projections.ResourcePriceSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * LEZIONE 1: dalle query derivate dal nome del metodo alle query JPQL esplicite.
 * Spring crea l'implementazione di questa interfaccia: non scriviamo una classe DAO.
 * JpaRepository offre già findAll, findById, save, delete... Qui aggiungiamo ricerche.
 *
 * Nei nomi dei metodi e in JPQL usiamo gli ATTRIBUTI JAVA (baseMarketPrice),
 * non le colonne SQL (base_market_price). In JPQL Resource è il nome dell'entità,
 * r è un alias e "select r" restituisce oggetti Resource, non singole colonne.
 * I metodi con suffisso Jpql sono alternative didattiche ai metodi derivati vicini:
 * grazie a @Query il nome del metodo non deve descrivere la sintassi della ricerca.
 * Salvo diversa indicazione, passare parametri non null. Una lista vuota significa
 * "nessun risultato"; non serve restituire null. Senza ORDER BY l'ordine non è garantito.
 */
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    // 1. UN FILTRO ALLA VOLTA: uguaglianza, testo, booleani e intervalli.

    /** Cerca il nome esatto, distinguendo maiuscole/minuscole. Il vincolo UNIQUE sul
     * nome giustifica Optional: possiamo trovare zero oppure una sola risorsa. */
    Optional<Resource> findByName(String name);

    /** Stessa ricerca in JPQL. @Param collega l'argomento a :name; il valore viene
     * passato come parametro, non concatenato nel testo della query. */
    @Query("select r from Resource r where r.name = :name")
    Optional<Resource> findByNameJpql(@Param("name") String name);

    /** Containing cerca una sottostringa, IgnoreCase ignora il caso: "aS" trova "Assi".
     * Spring aggiunge i caratteri jolly e tratta % e _ nell'input come testo letterale.
     * Scrivere a mano LIKE concat('%', :text, '%') senza escape NON sarebbe equivalente. */
    List<Resource> findByNameContainingIgnoreCase(String text);

    /** True è una condizione fissa: non occorre un parametro booleano.
     * processed è un Boolean nullable: true, false e null sono tre casi distinti. */
    List<Resource> findByProcessedTrue();

    /** BETWEEN include entrambi gli estremi. Per gli importi usiamo BigDecimal;
     * per esempio new BigDecimal("1200.00"), evitando gli arrotondamenti di double. */
    List<Resource> findByBaseMarketPriceBetween(BigDecimal minimum, BigDecimal maximum);

    // 2. CONDIZIONI COMPOSTE, LIMITI E RISULTATI SCALARI.

    /** Risorse lavorate con prezzo strettamente minore del limite: entrambe le
     * condizioni devono essere vere (AND). Il prezzo uguale al limite è escluso. */
    List<Resource> findByProcessedTrueAndBaseMarketPriceLessThan(BigDecimal maximum);

    /** Equivalente del metodo precedente: l'alias rende leggibili le condizioni. */
    @Query("""
            select r from Resource r
            where r.processed = true and r.baseMarketPrice < :maximum
            """)
    List<Resource> findAffordableProcessedResourcesJpql(BigDecimal maximum);

    /** Al massimo tre risorse, dalla più cara. A parità di prezzo ordiniamo per id:
     * il secondo criterio rende il risultato deterministico. Non è una pagina. */
    List<Resource> findTop3ByOrderByBaseMarketPriceDescIdAsc();

    /** Risponde a una domanda sì/no senza chiedere al chiamante di caricare una lista. */
    boolean existsByName(String name);

    /** Conta le risorse lavorate. Un conteggio si rappresenta con long, non con una lista. */
    long countByProcessedTrue();

    // 3. PAGINAZIONE E ORDINAMENTO DINAMICO.

    /** Page contiene contenuto, totale elementi e totale pagine; può richiedere una
     * seconda query COUNT. Pageable contiene numero pagina (da 0), dimensione e Sort.
     * Esempio: PageRequest.of(0, 5, Sort.by("baseMarketPrice").and(Sort.by("id"))).
     * Il parametro processed deve essere non null per confrontare questa coppia. */
    Page<Resource> findByProcessed(Boolean processed, Pageable pageable);

    /** Stessa pagina in JPQL. Per una SELECT semplice Spring ricava il COUNT.
     * Non scriviamo LIMIT/OFFSET in JPQL: Spring applica Pageable alla query.
     * Usiamo lo stesso Pageable nelle due versioni per confrontare anche l'ordine. */
    @Query("select r from Resource r where r.processed = :processed")
    Page<Resource> findByProcessedJpql(@Param("processed") Boolean processed, Pageable pageable);

    /** Slice sa se esiste una porzione successiva, ma non espone il totale.
     * È adatta a "carica altri": non richiede il conteggio complessivo delle righe. */
    Slice<Resource> findByBaseMarketPriceGreaterThan(BigDecimal minimum, Pageable pageable);

    // 4. OLTRE LA DERIVAZIONE DEL NOME: sottoquery e report aggregati.

    /** Risorse più care della media di TUTTE le risorse. La sottoquery non dipende
     * dalla riga esterna: è non correlata. Non basta un suffisso GreaterThan per
     * chiedere al database di calcolare la media nella stessa interrogazione. */
    @Query("""
            select r from Resource r
            where r.baseMarketPrice > (select avg(other.baseMarketPrice) from Resource other)
            order by r.baseMarketPrice desc, r.id asc
            """)
    List<Resource> findPricedAboveAverageJpql();

    /** Risorse che nessun TIPO di edificio produce (anche se quel tipo non è costruito).
     * NOT EXISTS verifica l'assenza di righe; la sottoquery è correlata tramite r.
     * Un outputResource null non elimina erroneamente tutte le risorse, come potrebbe
     * accadere con NOT IN su una sottoquery che restituisce anche null. */
    @Query("""
            select r from Resource r
            where not exists (
                select bt.id from BuildingType bt where bt.outputResource = r
            )
            order by r.id
            """)
    List<Resource> findWithoutProducerJpql();

    /** Una riga per valore di processed, incluso un eventuale gruppo null.
     * Gli alias corrispondono ai getter della proiezione, non a campi di Resource.
     * COUNT restituisce Long; AVG restituisce Double anche partendo da BigDecimal. */
    @Query("""
            select r.processed as processed, count(r) as resourceCount,
                   avg(r.baseMarketPrice) as averagePrice
            from Resource r
            group by r.processed
            """)
    List<ResourcePriceSummary> summarizePricesJpql();
}
