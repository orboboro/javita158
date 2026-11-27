/**
 * Laboratorio COMPANY: query Spring Data JPA da scrivere, senza soluzioni già dichiarate.
 *
 * <h2>Da dove iniziare</h2>
 * <p>Le otto entity sono già mappate in model.company, adattate dall'esercizio di
 * CappelloMagico. I repository estendono JpaRepository: sono pronti per l'iniezione,
 * ma contengono soltanto TODO per le nuove query. Ogni TODO richiede di aggiungere
 * una dichiarazione di metodo; non un corpo Java che filtra findAll() con uno stream.</p>
 * <p>Prima di scrivere nuove query, provare i metodi ereditati findAll(), findById(id),
 * count() ed existsById(id). Non vanno ridefiniti. findById restituisce Optional:
 * provare sia un id esistente sia uno assente. Per OrderDetail la chiave è un
 * OrderDetailId con DUE valori, orderId e productId.</p>
 *
 * <h2>Percorso consigliato: completare un livello prima di passare al successivo</h2>
 * <ol>
 *   <li>Livello 1: CA1, CU1-CU2, SU1, SH1, PR1-PR2, EM1-EM2, OR1.
 *       Uguaglianza, confronto numerico e null; nessun JOIN da scrivere.</li>
 *   <li>Livello 2: CA2, CU3-CU4, SU2-SU3, SH2, PR3, EM3, OR2, OD1.
 *       Testo, AND, intervalli, date, IN, conteggi e ordinamento.</li>
 *   <li>Livello 3: CA3, CU5, SU4, PR4-PR5, EM4-EM5, OR3-OR4, OD2-OD3.
 *       Prime coppie derivata/JPQL, relazioni, parentesi e LEFT JOIN.</li>
 *   <li>Livello 4: CU6, PR6-PR7, OR5-OR6. Page, Slice, più relazioni e JOIN FETCH.</li>
 *   <li>Livello 5: CA4, CU7-CU8, SU5, SH3, PR8-PR9, EM6, OR7, OD4-OD5.
 *       EXISTS, sottoquery correlate, aggregazioni e report: livello simile agli
 *       esempi avanzati dei repository Tropico.</li>
 * </ol>
 * <p>I livelli sono distribuiti fra i file: non occorre completare tutti gli
 * esercizi di un repository prima di aprire il successivo. I DTO per i report sono
 * già disponibili nel sottopackage projections come classi con costruttore e getter.</p>
 *
 * <h2>Due modi di dichiarare una ricerca</h2>
 * <ul>
 *   <li>DERIVATA: scegliere il nome del metodo usando gli attributi Java. Spring
 *       Data interpreta quel nome. Non è un linguaggio SQL separato e non serve @Query.</li>
 *   <li>JPQL: scegliere un nome descrittivo (per esempio con suffisso Jpql) e scrivere
 *       @Query. Usare classi e attributi Java, con parametri :nome e @Param("nome").</li>
 * </ul>
 * <p>Import utili: java.util.List/Optional/Collection, java.math.BigDecimal,
 * java.time.LocalDateTime, org.springframework.data.domain.Page/Slice/Pageable,
 * org.springframework.data.jpa.repository.Query e org.springframework.data.repository.query.Param.</p>
 * <p>Non aggiungere metodi chiamati semplicemente esercizio1() o @Query vuote come
 * segnaposto: Spring cerca di interpretarli all'avvio. Scrivere un metodo completo
 * alla volta. Se il contesto non parte, leggere il primo errore e le sue cause.</p>
 * <p>Una query derivata può attraversare relazioni: un JOIN non obbliga a usare JPQL.
 * Per i report con SELECT NEW indicare il nome completo della classe DTO e rispettare
 * ordine e tipi del costruttore: COUNT restituisce Long, AVG Double, SUM(Integer) Long.
 * Il DTO EmployeeManagerView ammette managerLastName null.</p>
 *
 * <h2>Attributi e relazioni da conoscere</h2>
 * <ul>
 *   <li>Product: productName, unitPrice, discontinued (Integer: 0/1), category, supplier.</li>
 *   <li>Category: categoryName, description. Supplier/Shipper: companyName.</li>
 *   <li>Customer: companyName, country, city, region, orders.</li>
 *   <li>Employee: firstName, lastName, hireDate, manager (facoltativo).</li>
 *   <li>Order: customer (facoltativo), employee, shipper, orderDate, requiredDate,
 *       shippedDate (facoltativa), freight, shipCountry.</li>
 *   <li>OrderDetail: order, product, unitPrice, quantity, discount; id è OrderDetailId.</li>
 * </ul>
 * <p>Le date sono LocalDateTime. I prezzi e gli sconti sono BigDecimal. Le proprietà
 * usano camelCase: unitPrice è Java, unitprice è SQL; quantity è Java, qty è SQL.
 * Customer.orders è LAZY. Per leggere relazioni lazy, usare una transazione nel
 * servizio o nel test; l'esercizio JOIN FETCH mostra come caricarle esplicitamente.</p>
 *
 * <h2>Come controllare ogni esercizio</h2>
 * <ol>
 *   <li>Scegliere parametri usando i dati effettivi di company nel client PostgreSQL.</li>
 *   <li>Scrivere prima su carta gli id attesi; poi chiamare il repository in un test
 *       Spring con profilo company e confrontare il risultato. Un test può usare
 *       @Transactional(readOnly = true) per leggere anche le relazioni lazy.</li>
 *   <li>Provare anche un valore assente, un confine incluso/escluso e, dove ammesso,
 *       una relazione null. Per IN usare collezioni non vuote; gli altri parametri
 *       sono non null e gli intervalli hanno inizio non successivo alla fine.</li>
 *   <li>Nelle coppie derivata/JPQL confrontare gli stessi risultati. Senza ordinamento
 *       confrontare insiemi di id; quando l'ordine è richiesto confrontare la sequenza.</li>
 * </ol>
 * <p>Per controlli ragionati: con prezzi 10, 20 e 30, l'intervallo inclusivo [10, 20]
 * seleziona i primi due, mentre la soglia stretta minore di 20 seleziona solo 10.
 * Un dipendente senza manager deve comparire nel LEFT JOIN con il nome manager null.
 * Due dettagli con quantità 3 e 5 rappresentano 8 pezzi ma soltanto 2 righe.</p>
 * <p>Per Page usare numeri da 0, dimensione positiva e ordine stabile con id come
 * secondo criterio. Non confondere getNumberOfElements() (pagina) e getTotalElements()
 * (intera ricerca). Eseguire almeno una pagina piena per controllare il conteggio.
 * I test CompanyRepositoryExercisesTest verificano solo mapping, lettura e isolamento
 * dei profili: non correggono automaticamente le soluzioni che ancora non esistono.</p>
 *
 * <h2>Avvio: un database alla volta, nello stesso progetto</h2>
 * <p>PostgreSQL deve essere avviato e company già importato sul server della demo.
 * application-company.properties cambia l'URL, mentre CompanyJpaConfiguration
 * seleziona soltanto entity e repository company. Cambiare SOLO l'URL non basta:
 * Hibernate cercherebbe anche le tabelle del dominio sbagliato.</p>
 * <pre>{@code
 * // Dalla cartella del pom.xml:
 * mvn spring-boot:run -Dspring-boot.run.profiles=company
 * mvn spring-boot:run -Dspring-boot.run.profiles=jpa
 * }</pre>
 * <p>Il profilo predefinito resta jpa (Tropico). Nell'IDE usare
 * --spring.profiles.active=company come argomento del programma.
 * Selezionare un solo profilo applicativo: company, jpa oppure in-memory.
 * Se company è attivo, i componenti Tropico sono esclusi anche se si aggiunge jpa.
 * Il profilo company offre i repository per gli esercizi, senza nuovi endpoint HTTP.</p>
 * <p>COMPANY_DB_URL permette un altro URL. Le credenziali sono ereditate da
 * application.properties e si possono sovrascrivere con SPRING_DATASOURCE_USERNAME
 * e SPRING_DATASOURCE_PASSWORD. Il profilo company usa validate e non esegue script
 * SQL di inizializzazione: verifica il database esistente senza modificarne lo schema.</p>
 * <pre>{@code
 * // Test di lettura del database company esistente:
 * mvn -B -Dtest=CompanyRepositoryExercisesTest test
 * // Tutti i test: servono company e il consueto tropico_test.
 * mvn -B test
 * }</pre>
 *
 * <h2>E se servissero entrambi i database contemporaneamente?</h2>
 * <p>Servirebbero due DataSource, due EntityManagerFactory e due transaction manager.
 * Ogni @EnableJpaRepositories dovrebbe indicare il proprio package, entityManagerFactoryRef
 * e transactionManagerRef. I servizi sceglierebbero il transaction manager appropriato.
 * Due transazioni locali non diventano automaticamente una transazione atomica unica.
 * Questa configurazione è un argomento successivo: qui i profili mantengono un solo
 * DataSource e una sola unità di persistenza per esecuzione.</p>
 * <p>Riferimenti:
 * <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html">Query Spring Data JPA</a>,
 * <a href="https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html">Proiezioni</a>,
 * <a href="https://docs.spring.io/spring-boot/how-to/data-access.html">Scansione entity e più datasource</a>.</p>
 */
package org.generation.italy.javita158.repositories.company;
