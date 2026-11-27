package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.projections.ResourcePriceSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/** Ogni test costruisce una piccola domanda con una risposta nota, senza usare il seed. */
class ResourceRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ResourceRepository repository;

    @Test
    void exactLookupAndJpqlAgreeIncludingWhenNothingMatches() {
        resource(1, "Rum", "100.00", true);
        resource(2, "Mais", "50.00", false);
        flushAndClear();

        assertThat(repository.findByName("Rum")).get().extracting(Resource::getId).isEqualTo(1);
        assertThat(repository.findByNameJpql("Rum")).get().extracting(Resource::getId).isEqualTo(1);
        assertThat(repository.findByName("rum")).isEmpty();
        assertThat(repository.findByNameJpql("rum")).isEmpty();
        assertThat(repository.existsByName("Rum")).isTrue();
        assertThat(repository.existsByName("Assente")).isFalse();
    }

    @Test
    void containingIgnoresCaseButTreatsSqlWildcardsAsLiteralCharacters() {
        resource(1, "Gold_ore", "100", false);
        resource(2, "GoldXore", "100", false);
        resource(3, "Gold 50%", "100", false);
        flushAndClear();

        assertThat(repository.findByNameContainingIgnoreCase("gOLd"))
                .extracting(Resource::getId).containsExactlyInAnyOrder(1, 2, 3);
        // Senza escape, '_' in LIKE corrisponderebbe anche alla X di GoldXore.
        assertThat(repository.findByNameContainingIgnoreCase("_"))
                .extracting(Resource::getId).containsExactly(1);
        assertThat(repository.findByNameContainingIgnoreCase("%"))
                .extracting(Resource::getId).containsExactly(3);
    }

    @Test
    void betweenIncludesBoundsWhileLessThanExcludesTheUpperBound() {
        resource(1, "A", "100", true);
        resource(2, "B", "200", true);
        resource(3, "C", "100", false);
        resource(4, "D", "100", null);
        resource(5, "E", "300", false);
        flushAndClear();

        assertThat(repository.findByBaseMarketPriceBetween(new BigDecimal("100"), new BigDecimal("200")))
                .extracting(Resource::getId).containsExactlyInAnyOrder(1, 2, 3, 4);
        assertThat(repository.findByProcessedTrue()).extracting(Resource::getId).containsExactlyInAnyOrder(1, 2);
        assertThat(repository.countByProcessedTrue()).isEqualTo(2);
        assertThat(repository.findByProcessedTrueAndBaseMarketPriceLessThan(new BigDecimal("200")))
                .extracting(Resource::getId).containsExactly(1);
        assertThat(repository.findAffordableProcessedResourcesJpql(new BigDecimal("200")))
                .extracting(Resource::getId).containsExactly(1);
    }

    @Test
    void topThreeUsesIdToBreakPriceTies() {
        resource(1, "A", "300", true);
        resource(2, "B", "300", true);
        resource(3, "C", "100", false);
        resource(4, "D", "500", false);
        flushAndClear();

        assertThat(repository.findTop3ByOrderByBaseMarketPriceDescIdAsc())
                .extracting(Resource::getId).containsExactly(4, 1, 2);
    }

    @Test
    void pagesAgreeOnContentTotalsTieBreakingAndAnOutOfRangePage() {
        resource(1, "A", "100", true);
        resource(2, "B", "200", true);
        resource(3, "C", "200", true);
        resource(4, "D", "300", true);
        resource(5, "E", "400", true);
        resource(6, "Non lavorata", "900", false);
        resource(7, "Stato ignoto", "900", null);
        flushAndClear();

        Sort order = Sort.by("baseMarketPrice").descending().and(Sort.by("id"));
        int[][] expectedIds = {{5, 4}, {2, 3}, {1}, {}};
        for (int index = 0; index < expectedIds.length; index++) {
            var pageable = PageRequest.of(index, 2, order);
            Page<Resource> derived = repository.findByProcessed(true, pageable);
            Page<Resource> jpql = repository.findByProcessedJpql(true, pageable);
            // Controlliamo una risposta attesa, non solo che due query abbiano lo stesso errore.
            var expected = java.util.Arrays.stream(expectedIds[index]).boxed().toList();
            assertThat(derived.getContent()).extracting(Resource::getId).containsExactlyElementsOf(expected);
            assertThat(jpql.getContent()).extracting(Resource::getId).containsExactlyElementsOf(expected);
            assertThat(derived.getTotalElements()).isEqualTo(5);
            assertThat(jpql.getTotalElements()).isEqualTo(5);
            assertThat(derived.getTotalPages()).isEqualTo(3);
            assertThat(jpql.getTotalPages()).isEqualTo(3);
            assertThat(derived.hasNext()).isEqualTo(index < 2);
            assertThat(jpql.hasNext()).isEqualTo(index < 2);
        }
    }

    @Test
    void sliceReportsWhetherThereAreMoreResultsWithoutExposingTotals() {
        resource(1, "A", "100", false);
        resource(2, "B", "200", false);
        resource(3, "C", "300", false);
        resource(4, "D", "400", false);
        flushAndClear();

        var first = repository.findByBaseMarketPriceGreaterThan(new BigDecimal("100"),
                PageRequest.of(0, 2, Sort.by("id")));
        assertThat(first.getContent()).extracting(Resource::getId).containsExactly(2, 3);
        assertThat(first.hasNext()).isTrue();
        var last = repository.findByBaseMarketPriceGreaterThan(new BigDecimal("100"), first.nextPageable());
        assertThat(last.getContent()).extracting(Resource::getId).containsExactly(4);
        assertThat(last.hasNext()).isFalse();
    }

    @Test
    void averageQueryComparesAgainstAllResources() {
        resource(1, "A", "100", false);
        resource(2, "B", "200", true);
        resource(3, "C", "600", true);
        flushAndClear();

        // Media = 300; solo 600 è strettamente sopra.
        assertThat(repository.findPricedAboveAverageJpql()).extracting(Resource::getId).containsExactly(3);
    }

    @Test
    void noProducerQueryHandlesNullOutputsAndResourcesUsedOnlyAsInputs() {
        Resource input = resource(1, "Legname test", "100", false);
        Resource output = resource(2, "Assi test", "200", true);
        resource(3, "Oro test", "300", false);
        buildingType(1, "Segheria test", "Industry", 1000, 4, input, output);
        buildingType(2, "Casa test", "Housing", 200, 0, null, null);
        flushAndClear();

        assertThat(repository.findWithoutProducerJpql()).extracting(Resource::getId).containsExactly(1, 3);
    }

    @Test
    void groupedReportIncludesNullStatusAsItsOwnGroup() {
        resource(1, "A", "100", false);
        resource(2, "B", "300", false);
        resource(3, "C", "200", true);
        resource(4, "D", "400", null);
        flushAndClear();

        var report = repository.summarizePricesJpql();
        assertThat(report).hasSize(3);
        ResourcePriceSummary raw = report.stream().filter(r -> Boolean.FALSE.equals(r.getProcessed())).findFirst().orElseThrow();
        assertThat(raw.getResourceCount()).isEqualTo(2);
        assertThat(raw.getAveragePrice()).isEqualTo(200.0);
        assertThat(report).filteredOn(r -> r.getProcessed() == null).singleElement()
                .satisfies(r -> {
                    assertThat(r.getResourceCount()).isEqualTo(1);
                    assertThat(r.getAveragePrice()).isEqualTo(400.0);
                });
    }

    @Test
    void emptyDatabaseProducesEmptyListsReportsAndPages() {
        assertThat(repository.findPricedAboveAverageJpql()).isEmpty();
        assertThat(repository.summarizePricesJpql()).isEmpty();
        assertThat(repository.findByProcessed(true, PageRequest.of(0, 2)).getTotalElements()).isZero();
        assertThat(repository.findByProcessedJpql(true, PageRequest.of(0, 2)).getContent()).isEmpty();
    }
}
