/**
 * Laboratorio di query Spring Data JPA e JPQL, sul dominio Tropico.
 *
 * <h2>Come studiare questi esempi</h2>
 * <ol>
 *   <li>ResourceRepository: filtri semplici, AND, ordinamento, Page/Slice e prime sottoquery.</li>
 *   <li>FactionRepository: testo, null ed EXISTS/NOT EXISTS su relazioni non bidirezionali.</li>
 *   <li>BuildingTypeRepository: IN, percorsi di proprietà, GROUP BY/HAVING e proiezioni.</li>
 *   <li>BuildingRepository: JOIN, conteggi correlati e JOIN FETCH.</li>
 *   <li>CitizenRepository: condizioni con parentesi, più JOIN, countQuery e report.</li>
 * </ol>
 * <p>Per ogni metodo: leggere la domanda, prevedere il risultato, leggere la query,
 * poi aprire il test omonimo del repository. Le fixture del test rendono controllabile
 * il ragionamento senza conoscere i 94 cittadini del database dimostrativo.</p>
 *
 * <h2>Tre nomi da distinguere</h2>
 * <ul>
 *   <li>Attributo Java: Resource.baseMarketPrice.</li>
 *   <li>Query derivata: findByBaseMarketPriceBetween(...).</li>
 *   <li>JPQL: select r from Resource r where r.baseMarketPrice between :minimum and :maximum.</li>
 * </ul>
 * <p>La colonna SQL base_market_price non compare nei nomi derivati né in JPQL.
 * Il provider JPA traduce la query in SQL. Spring Data costruisce l'implementazione
 * del repository e lega i parametri. Non filtriamo findAll() con uno stream Java:
 * lasciamo al database il filtro, l'ordinamento e la selezione della pagina.</p>
 * <p>I metodi con suffisso Jpql mostrano l'alternativa esplicita. Le coppie hanno lo
 * stesso significato per gli input documentati. Non ogni query lunga richiede JPQL:
 * i nomi derivati attraversano relazioni. Sono sottoquery, aggregazioni e gruppi a
 * superare questo meccanismo; esistono anche altre API, come Criteria/Specifications,
 * che non sono l'argomento di questo laboratorio.</p>
 *
 * <h2>Page, Slice e Sort</h2>
 * <pre>{@code
 * Pageable request = PageRequest.of(0, 5,
 *         Sort.by("baseMarketPrice").descending().and(Sort.by("id")));
 * Page<Resource> page = resourceRepository.findByProcessed(true, request);
 * List<Resource> resources = page.getContent();
 * long total = page.getTotalElements();
 * int pages = page.getTotalPages();
 * boolean more = page.hasNext();
 * }</pre>
 * <p>0 significa prima pagina, non prima riga; la dimensione deve essere positiva.
 * Una pagina oltre la fine ha contenuto vuoto. Il totale descrive tutte le righe
 * corrispondenti al filtro, non solo quelle presenti nella pagina corrente.
 * Il secondo criterio id risolve i pareggi. Se i dati cambiano fra richieste, la
 * paginazione per offset può comunque spostare righe da una pagina all'altra.</p>
 * <p>Page può eseguire un COUNT; Slice cerca se esistono altri risultati ma non offre
 * il totale. Top3 è un limite fisso e non fornisce metadati di pagina. Pageable non
 * è un parametro del WHERE: Spring lo usa per applicare ordinamento e limiti.</p>
 * <p>Nel CitizenRepository la countQuery rende esplicito che SELECT e COUNT devono
 * usare gli stessi filtri. Un report con GROUP BY conta gruppi: non riutilizzare
 * automaticamente il conteggio delle righe originarie. Evitare inoltre la pagina
 * su un fetch join di collezioni: duplicazione delle righe e caricamento in memoria
 * possono rendere limiti e prestazioni diversi da quelli attesi. Qui il FETCH è
 * su una ManyToOne e l'esempio non è paginato.</p>
 *
 * <h2>Le domande da farsi sui risultati</h2>
 * <ul>
 *   <li>Il limite è incluso (Between/LessThanEqual) o escluso (LessThan)?</li>
 *   <li>Un INNER JOIN deve eliminare chi non ha la relazione, o serve LEFT JOIN?</li>
 *   <li>Un null significa assenza/sconosciuto: è davvero equivalente a zero o false?</li>
 *   <li>COUNT conta righe, AVG ignora i valori null: quali persone entrano nella media?</li>
 *   <li>La SELECT restituisce entità oppure righe di un report?</li>
 *   <li>L'ordine è esplicito e gestisce i pareggi?</li>
 * </ul>
 * <p>I repository contengono la logica di accesso ai dati; un servizio li invoca tramite
 * dependency injection e un controller riceve/valida i parametri HTTP. Per esporre
 * le nuove ricerche si può mantenere questa stessa catena controller-servizio-repository.
 * La paginazione non richiede di spostare le query nel controller.</p>
 *
 * <h2>Riferimenti ufficiali</h2>
 * <ul>
 *   <li><a href="https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html">Query derivate e @Query</a></li>
 *   <li><a href="https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html">Percorsi, ordinamento e paginazione</a></li>
 *   <li><a href="https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html">Proiezioni</a></li>
 * </ul>
 */
package org.generation.italy.javita158.repositories.tropico;
