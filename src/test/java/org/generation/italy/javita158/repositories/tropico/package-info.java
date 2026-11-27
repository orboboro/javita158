/**
 * Test delle query con fixture autonome e rollback automatico.
 *
 * <h2>Preparazione: una volta sola</h2>
 * <p>Usiamo il server PostgreSQL già in esecuzione per la demo, senza avviare altri
 * container. Creare al suo interno un database vuoto chiamato tropico_test, posseduto
 * dallo stesso utente configurato in application.properties. Per esempio, da terminale:</p>
 * <pre>{@code
 * createdb --host=localhost --port=5432 --username=postgresMaster --password tropico_test
 * }</pre>
 * <p>Il comando chiede la password senza salvarla nella cronologia. In alternativa,
 * eseguire CREATE DATABASE tropico_test dal proprio client SQL, fuori da una transazione.
 * Non importare il file con i dati dimostrativi in questo database.</p>
 * <p>application-test.properties sceglie questo database. Lo script di test contiene
 * solo il DDL della demo, con IF NOT EXISTS per riusare lo schema fra contesti Spring.
 * I vincoli CHECK e le foreign key rimangono quelli reali. Hibernate valida le entity.
 * Se il DDL cambia, aggiornare anche lo schema di test; IF NOT EXISTS non migra tabelle
 * già esistenti. Non cambiare l'URL di test per puntare al database della lezione.</p>
 *
 * <h2>Esecuzione con Java 27 e PostgreSQL attivo</h2>
 * <pre>{@code
 * mvn test
 * mvn -Dtest=ResourceRepositoryTest test
 * mvn -Dtest='*RepositoryTest' test
 * }</pre>
 * <p>I test non sono disabilitati se il database è spento: un errore di connessione
 * segnala che manca un prerequisito. I test di controller/servizio che usano mock
 * restano eseguibili senza PostgreSQL.</p>
 *
 * <h2>Cosa accade per ogni test</h2>
 * <ol>
 *   <li>@BeforeTransaction verifica che non siano rimaste righe da altri test.</li>
 *   <li>@DataJpaTest apre la transazione.</li>
 *   <li>Il test inserisce soltanto i dati che servono alla propria domanda.</li>
 *   <li>flushAndClear invia gli INSERT e svuota la cache delle entity.</li>
 *   <li>La query viene eseguita e il risultato viene confrontato con valori attesi.</li>
 *   <li>Spring effettua il rollback, anche in caso di asserzione fallita.</li>
 *   <li>@AfterTransaction verifica dal database che le righe siano scomparse.</li>
 * </ol>
 * <p>Non ci sono DELETE di pulizia, TRUNCATE, @Commit, dipendenze dall'ordine dei test
 * o dati copiati dal database della demo. Non eseguire più suite in parallelo sullo
 * stesso database di test. Le sequenze PostgreSQL avanzano anche se la transazione
 * viene annullata: confrontiamo gli id restituiti, senza aspettarci che ripartano da 1.</p>
 * <p>Per studiare l'SQL generato, impostare logging.level.org.hibernate.SQL=DEBUG
 * e logging.level.org.hibernate.orm.jdbc.bind=TRACE in application-test.properties.
 * Confrontare query derivate e JPQL anche nei test: l'equivalenza non richiede che
 * Hibernate generi stringhe SQL identiche, ma che restituiscano gli stessi risultati.</p>
 *
 * @see org.generation.italy.javita158.repositories.tropico.RepositoryTestSupport
 */
package org.generation.italy.javita158.repositories.tropico;
