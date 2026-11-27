package org.generation.italy.javita158.repositories.tropico;

import jakarta.persistence.EntityManager;
import org.generation.italy.javita158.model.tropico.Building;
import org.generation.italy.javita158.model.tropico.BuildingType;
import org.generation.italy.javita158.model.tropico.Citizen;
import org.generation.italy.javita158.model.tropico.Faction;
import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Infrastruttura comune; i dati specifici rimangono visibili nei singoli test.
 *
 * {@code @DataJpaTest} carica entity/repository e apre una transazione PER TEST, annullata
 * automaticamente anche se un'asserzione fallisce. Non servono deleteAll, @AfterEach,
 * @Commit o ordine di esecuzione. Anche gli inserimenti del setup partecipano alla
 * stessa transazione. I test non fanno richieste HTTP su un altro thread.
 *
 * Usiamo tropico_test nello stesso PostgreSQL della demo, non un mock del repository.
 * Non avviamo altri container. Il database di test non contiene i dati della demo:
 * è importante anche per AVG, COUNT e paginazione, che leggono tutte le righe.
 * Le fixture sono salvate con EntityManager per non dipendere dalle query da testare.
 * flushAndClear manda gli INSERT al database e svuota la cache di primo livello:
 * le ricerche successive devono leggere i dati dal database.
 *
 * Attenzione: in PostgreSQL le sequenze NON fanno rollback. Gli id generati possono
 * avere salti; i test leggono l'id assegnato all'oggetto, non presumono id = 1.
 */
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"test", "jpa"})
abstract class RepositoryTestSupport {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    /** Eseguito FUORI dalla transazione del test: verifica isolamento iniziale e rollback
     * finale. Non cancella dati e non maschera un test che ha lasciato righe committate. */
    @BeforeTransaction
    @AfterTransaction
    void databaseContainsNoFixtureRows() {
        for (String table : new String[]{"citizen", "building", "buildingtype", "faction", "resource"}) {
            // I nomi delle tabelle sono costanti interne, mai input dell'utente.
            assertThat(jdbc.queryForObject("select count(*) from " + table, Long.class))
                    .as("La tabella %s deve essere vuota prima e dopo ogni test", table)
                    .isZero();
        }
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    // Factory piccole: riducono il rumore dei costruttori, non nascondono un dataset globale.

    protected Resource resource(int id, String name, String price, Boolean processed) {
        Resource resource = new Resource(id, name, new BigDecimal(price), processed);
        entityManager.persist(resource);
        return resource;
    }

    protected Faction faction(int id, String name, String description) {
        Faction faction = new Faction(id, name, description);
        entityManager.persist(faction);
        return faction;
    }

    protected BuildingType buildingType(int id, String name, String category, int cost,
                                        int workers, Resource input, Resource output) {
        BuildingType type = new BuildingType(id, name, category, cost, new BigDecimal("10.00"),
                workers, input, output, null);
        entityManager.persist(type);
        return type;
    }

    protected Building building(BuildingType type, int budget, boolean active) {
        Building building = new Building(null, type, budget, active);
        entityManager.persist(building);
        return building;
    }

    /** Valori validi rispetto ai CHECK del DDL. Il test può modificare i campi non
     * presenti fra i parametri prima di flushAndClear (l'oggetto è ancora managed). */
    protected Citizen citizen(String name, int age, String salary, Building job,
                              Faction faction, Boolean rebel, Integer happiness) {
        Citizen citizen = new Citizen(null, name, "Test", "N", age, "HighSchool", job,
                salary == null ? null : new BigDecimal(salary), null, "Poor", faction, rebel, happiness);
        entityManager.persist(citizen);
        return citizen;
    }
}
