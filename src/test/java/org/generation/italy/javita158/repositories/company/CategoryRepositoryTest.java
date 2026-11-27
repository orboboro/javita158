package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Esempio per CA1: prepara i dati, chiama il repository, verifica il risultato.
 *
 * @DataJpaTest carica entity e repository e apre una transazione per ogni test.
 * Alla fine fa ROLLBACK, anche se il test fallisce: le categorie inserite scompaiono.
 * Non servono deleteAll(), @Commit o pulizie manuali; i dati esistenti restano intatti.
 *
 * @ActiveProfiles sceglie company indipendentemente dal profilo predefinito dell'app.
 * Replace.NONE usa il PostgreSQL configurato, senza sostituirlo con un database in memoria.
 * @Sql inserisce i dati prima di OGNI test, nella stessa transazione del test.
 * Usiamo id negativi riservati alle fixture: non dipendiamo dalla sequenza degli id,
 * che nel database importato può non essere allineata ai dati. Non la modifichiamo.
 * Anche i nomi __CA1_* sono riservati alle fixture (massimo 15 caratteri).
 * Il setup usa SQL: è indipendente dal metodo del repository che vogliamo verificare.
 *
 * Esecuzione: mvn -B -Dtest=CategoryRepositoryTest test
 */
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("company")
@Sql(statements = """
        insert into categories (categoryid, categoryname, description) values
            (-9101, '__CA1_MATCH__', 'Prima categoria del test'),
            (-9102, '__CA1_MATCH__', 'Seconda categoria del test'),
            (-9103, '__CA1_MATCH__X', 'Nome più lungo'),
            (-9104, '__ca1_match__', 'Nome in minuscolo');
        """)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void findByCategoryNameReturnsAllAndOnlyExactMatches() {
        // 1. ARRANGE: @Sql ha inserito due nomi uguali, uno più lungo e uno in minuscolo.

        // 2. ACT: chiamare il metodo che vogliamo verificare.
        var result = repository.findByCategoryName("__CA1_MATCH__");

        // 3. ASSERT: entrambe le corrispondenze, nessun altro risultato.
        // Non c'è ORDER BY: non presumiamo un ordine. Gli id sono quelli assegnati in @Sql.
        assertThat(result)
                .extracting(Category::getId)
                .containsExactlyInAnyOrder(-9101, -9102);
    }

    @Test
    void findByCategoryNameReturnsAnEmptyListWhenNameDoesNotExist() {
        // ARRANGE: @Sql ha inserito quattro categorie, nessuna con il nome cercato.

        // ACT
        var result = repository.findByCategoryName("__CA1_NONE__");

        // ASSERT: una ricerca senza corrispondenze restituisce una lista vuota, non null.
        assertThat(result).isEmpty();
    }
}
