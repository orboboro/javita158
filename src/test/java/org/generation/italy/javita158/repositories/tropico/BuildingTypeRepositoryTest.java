package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.BuildingType;
import org.generation.italy.javita158.model.tropico.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingTypeRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private BuildingTypeRepository repository;

    @Test
    void categoryInNullInputAndCostFiltersSelectTheExpectedTypes() {
        Resource wood = resource(1, "Legname test", "100", false);
        buildingType(1, "Industria", "Industry", 200, 2, wood, null);
        buildingType(2, "Casa", "Housing", 100, 0, null, null);
        buildingType(3, "Miniera", "Extraction", 300, 4, null, null);
        flushAndClear();

        assertThat(repository.findByCategory("Industry")).extracting(BuildingType::getId).containsExactly(1);
        assertThat(repository.findByCategoryJpql("Industry")).extracting(BuildingType::getId).containsExactly(1);
        assertThat(repository.findByCategory("industry")).isEmpty();
        assertThat(repository.findByCategoryJpql("industry")).isEmpty();
        assertThat(repository.findByCategoryIn(List.of("Housing", "Extraction")))
                .extracting(BuildingType::getId).containsExactlyInAnyOrder(2, 3);
        assertThat(repository.findByInputResourceIsNull())
                .extracting(BuildingType::getId).containsExactlyInAnyOrder(2, 3);
        assertThat(repository.findByConstructionCostLessThan(200))
                .extracting(BuildingType::getId).containsExactly(2);
    }

    @Test
    void outputRelationshipSearchAndExplicitJoinAgree() {
        Resource rum = resource(1, "Rum test", "300", true);
        Resource boards = resource(2, "Assi test", "200", true);
        buildingType(1, "Distilleria", "Industry", 200, 2, null, rum);
        buildingType(2, "Segheria", "Industry", 200, 2, null, boards);
        buildingType(3, "Casa", "Housing", 100, 0, null, null);
        flushAndClear();

        assertThat(repository.findByOutputResource_Name("Rum test"))
                .extracting(BuildingType::getId).containsExactly(1);
        assertThat(repository.findProducingResourceJpql("Rum test"))
                .extracting(BuildingType::getId).containsExactly(1);
        assertThat(repository.findByOutputResource_Name("Assente")).isEmpty();
        assertThat(repository.findProducingResourceJpql("Assente")).isEmpty();
    }

    @Test
    void costRangePagesIncludeBothBoundariesAndHaveTheSameTotals() {
        buildingType(1, "A", "Industry", 100, 2, null, null);
        buildingType(2, "B", "Industry", 200, 2, null, null);
        buildingType(3, "C", "Industry", 200, 2, null, null);
        buildingType(4, "D", "Industry", 300, 2, null, null);
        buildingType(5, "E", "Housing", 100, 0, null, null);
        flushAndClear();

        for (int index = 0; index < 2; index++) {
            var pageable = PageRequest.of(index, 2, Sort.by("constructionCost").and(Sort.by("id")));
            var derived = repository.findByCategoryAndConstructionCostBetween("Industry", 100, 200, pageable);
            var jpql = repository.findAffordableTypesJpql("Industry", 100, 200, pageable);
            List<Integer> expected = index == 0 ? List.of(1, 2) : List.of(3);
            assertThat(derived.getContent()).extracting(BuildingType::getId).containsExactlyElementsOf(expected);
            assertThat(jpql.getContent()).extracting(BuildingType::getId).containsExactlyElementsOf(expected);
            assertThat(derived.getTotalElements()).isEqualTo(3);
            assertThat(jpql.getTotalElements()).isEqualTo(3);
            assertThat(jpql.getTotalPages()).isEqualTo(2);
            assertThat(jpql.hasNext()).isEqualTo(index == 0);
        }
    }

    @Test
    void averageIsCalculatedPerCategoryRatherThanAcrossAllTypes() {
        buildingType(1, "Economico", "Industry", 100, 2, null, null);
        buildingType(2, "Costoso", "Industry", 300, 2, null, null);
        buildingType(3, "Casa unica", "Housing", 1000, 0, null, null);
        buildingType(4, "Senza categoria", null, 900, 0, null, null);
        flushAndClear();

        // Industry ha media 200; Housing ha media 1000 e nessun tipo la supera.
        assertThat(repository.findCostlierThanCategoryAverageJpql())
                .extracting(BuildingType::getId).containsExactly(2);
    }

    @Test
    void anInactiveBuildingStillMeansItsTypeHasBeenBuilt() {
        BuildingType built = buildingType(1, "Costruito", "Industry", 100, 2, null, null);
        buildingType(2, "Mai costruito", "Housing", 200, 0, null, null);
        building(built, 3, false);
        flushAndClear();

        assertThat(repository.findNeverBuiltJpql()).extracting(BuildingType::getId).containsExactly(2);
    }

    @Test
    void havingFiltersGroupsAndProjectionKeepsTheAggregateTypes() {
        BuildingType a = buildingType(1, "A", "Industry", 100, 2, null, null);
        a.setMaintenanceCost(new BigDecimal("12.50"));
        BuildingType b = buildingType(2, "B", "Industry", 300, 2, null, null);
        b.setMaintenanceCost(new BigDecimal("30.00"));
        buildingType(3, "Casa", "Housing", 900, 0, null, null);
        buildingType(4, "Senza categoria", null, 999, 0, null, null);
        flushAndClear();

        assertThat(repository.summarizeCategoriesJpql(2)).singleElement().satisfies(row -> {
            assertThat(row.getCategory()).isEqualTo("Industry");
            assertThat(row.getTypeCount()).isEqualTo(2);
            assertThat(row.getAverageConstructionCost()).isEqualTo(200.0);
            assertThat(row.getMaximumMaintenanceCost()).isEqualByComparingTo("30.00");
        });
        assertThat(repository.summarizeCategoriesJpql(3)).isEmpty();
    }
}
