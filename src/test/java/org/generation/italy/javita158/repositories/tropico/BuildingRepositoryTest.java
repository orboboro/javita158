package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Building;
import org.generation.italy.javita158.model.tropico.BuildingType;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private BuildingRepository repository;

    @Test
    void simpleFiltersAndCategoryJoinUseBuildingAttributesAndRelationships() {
        BuildingType industry = buildingType(1, "Fabbrica", "Industry", 100, 2, null, null);
        BuildingType housing = buildingType(2, "Casa", "Housing", 100, 0, null, null);
        Building low = building(industry, 1, true);
        Building high = building(industry, 5, false);
        Building middle = building(housing, 3, true);
        flushAndClear();

        assertThat(repository.findByActiveTrue()).extracting(Building::getId)
                .containsExactlyInAnyOrder(low.getId(), middle.getId());
        assertThat(repository.findByBudgetLevelBetween(1, 3)).extracting(Building::getId)
                .containsExactlyInAnyOrder(low.getId(), middle.getId());
        assertThat(repository.findByBuildingType_Category("Industry")).extracting(Building::getId)
                .containsExactlyInAnyOrder(low.getId(), high.getId());
        assertThat(repository.findInCategoryJpql("Industry")).extracting(Building::getId)
                .containsExactlyInAnyOrder(low.getId(), high.getId());
    }

    @Test
    void activeCategoryPagesExcludeOtherCategoriesAndInactiveBuildings() {
        BuildingType industry = buildingType(1, "Fabbrica", "Industry", 100, 2, null, null);
        BuildingType housing = buildingType(2, "Casa", "Housing", 100, 0, null, null);
        building(industry, 3, true);
        Building second = building(industry, 3, true);
        building(industry, 3, false);
        building(housing, 3, true);
        flushAndClear();

        var pageable = PageRequest.of(1, 1, Sort.by("id"));
        var derived = repository.findByActiveTrueAndBuildingType_Category("Industry", pageable);
        var jpql = repository.findActiveInCategoryJpql("Industry", pageable);
        assertThat(derived.getContent()).extracting(Building::getId).containsExactly(second.getId());
        assertThat(jpql.getContent()).extracting(Building::getId).containsExactly(second.getId());
        assertThat(derived.getTotalElements()).isEqualTo(2);
        assertThat(jpql.getTotalElements()).isEqualTo(2);
        assertThat(jpql.hasNext()).isFalse();
    }

    @Test
    void noWorkersMeansNoJobAssignmentEvenIfSomeoneLivesThere() {
        BuildingType type = buildingType(1, "Tipo", "Housing", 100, 0, null, null);
        Building empty = building(type, 3, true);
        Building occupied = building(type, 3, true);
        building(type, 3, false);
        citizen("Lavoratore", 30, "10", occupied, null, false, 60).setHomeBuilding(empty);
        citizen("Disoccupato", 30, "0", null, null, false, 60);
        flushAndClear();

        assertThat(repository.findActiveWithoutWorkersJpql())
                .extracting(Building::getId).containsExactly(empty.getId());
    }

    @Test
    void workerCountIsCorrelatedAndExistsDoesNotDuplicateBuildings() {
        BuildingType type = buildingType(1, "Fabbrica", "Industry", 100, 1, null, null);
        Building overstaffed = building(type, 3, true);
        Building atCapacity = building(type, 3, true);
        building(type, 3, true);
        citizen("Uno", 30, "10", overstaffed, null, false, 10);
        citizen("Due", 30, "10", overstaffed, null, false, 20);
        citizen("Tre", 30, "10", atCapacity, null, false, 40);
        citizen("Disoccupato", 30, "0", null, null, false, 10);
        flushAndClear();

        assertThat(repository.findOverstaffedJpql()).extracting(Building::getId).containsExactly(overstaffed.getId());
        assertThat(repository.findWithUnhappyWorkersJpql(40))
                .extracting(Building::getId).containsExactly(overstaffed.getId());
    }

    @Test
    void fetchJoinLoadsTheTypeSoItRemainsReadableAfterDetaching() {
        BuildingType type = buildingType(1, "Fabbrica", "Industry", 100, 2, null, null);
        Building active = building(type, 3, true);
        building(type, 3, false);
        flushAndClear();

        // Nessuna entity del setup è rimasta nella cache: FETCH deve inizializzare il tipo.
        var results = repository.findActiveWithTypeJpql();
        assertThat(results).extracting(Building::getId).containsExactly(active.getId());
        assertThat(Hibernate.isInitialized(results.getFirst().getBuildingType())).isTrue();
        entityManager.clear();
        assertThat(results.getFirst().getBuildingType().getName()).isEqualTo("Fabbrica");
    }
}
