package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Faction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class FactionRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private FactionRepository repository;

    @Test
    void exactAndPartialSearchesHaveDifferentSemantics() {
        faction(1, "Capitalist test", "Libero mercato");
        faction(2, "Communist test", null);
        flushAndClear();

        assertThat(repository.findByName("Capitalist test")).get().extracting(Faction::getId).isEqualTo(1);
        assertThat(repository.findByNameJpql("Capitalist test")).get().extracting(Faction::getId).isEqualTo(1);
        assertThat(repository.findByName("capitalist test")).isEmpty();
        assertThat(repository.findByNameJpql("capitalist test")).isEmpty();
        assertThat(repository.findByNameContainingIgnoreCase("CAP"))
                .extracting(Faction::getId).containsExactly(1);
        assertThat(repository.findByDescriptionContainingIgnoreCase("MERCATO"))
                .extracting(Faction::getId).containsExactly(1);
    }

    @Test
    void descriptionPagesIncludeEmptyStringsButExcludeNull() {
        faction(1, "A", "Descrizione");
        faction(2, "B", "");
        faction(3, "C", null);
        flushAndClear();

        var pageable = PageRequest.of(1, 1, Sort.by("name").and(Sort.by("id")));
        var derived = repository.findByDescriptionIsNotNull(pageable);
        var jpql = repository.findDescribedFactionsJpql(pageable);
        assertThat(derived.getContent()).extracting(Faction::getId).containsExactly(2);
        assertThat(jpql.getContent()).extracting(Faction::getId).containsExactly(2);
        assertThat(derived.getTotalElements()).isEqualTo(2);
        assertThat(jpql.getTotalElements()).isEqualTo(2);
        assertThat(jpql.hasNext()).isFalse();
    }

    @Test
    void existsDoesNotDuplicateFactionsAndNotExistsIgnoresUnaffiliatedCitizens() {
        Faction rebels = faction(1, "Ribelli", null);
        Faction peaceful = faction(2, "Pacifici", null);
        faction(3, "Vuota", null);
        citizen("Uno", 20, "10", null, rebels, true, 20);
        citizen("Due", 20, "10", null, rebels, true, 30);
        citizen("Tre", 20, "10", null, peaceful, false, 80);
        citizen("Senza fazione", 20, "10", null, null, true, 10);
        flushAndClear();

        assertThat(repository.findWithRebelSupportersJpql()).extracting(Faction::getId).containsExactly(1);
        assertThat(repository.findWithoutSupportersJpql()).extracting(Faction::getId).containsExactly(3);
    }

    @Test
    void correlatedAverageIgnoresNullHappinessAndExcludesEmptyGroups() {
        Faction low = faction(1, "Bassa", null);
        Faction boundary = faction(2, "Al limite", null);
        Faction unknown = faction(3, "Ignoto", null);
        faction(4, "Vuota", null);
        citizen("Basso", 20, "10", null, low, false, 20);
        citizen("Ignoto", 20, "10", null, low, false, null);
        citizen("Limite", 20, "10", null, boundary, false, 40);
        citizen("Tutto null", 20, "10", null, unknown, false, null);
        flushAndClear();

        // Bassa ha media 20 (non 10); la media 40 è esclusa dal confronto stretto.
        assertThat(repository.findWithAverageHappinessBelowJpql(40))
                .extracting(Faction::getId).containsExactly(1);
        assertThat(repository.findWithAverageHappinessBelowJpql(15)).isEmpty();
    }
}
