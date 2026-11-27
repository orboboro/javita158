package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Building;
import org.generation.italy.javita158.model.tropico.BuildingType;
import org.generation.italy.javita158.model.tropico.Citizen;
import org.generation.italy.javita158.model.tropico.Faction;
import org.generation.italy.javita158.model.tropico.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class CitizenRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private CitizenRepository repository;

    @Test
    void basicFiltersDistinguishAgeEducationMissingHomeAndMissingJob() {
        BuildingType type = buildingType(1, "Tipo", "Service", 100, 2, null, null);
        Building place = building(type, 3, true);
        Citizen adult = citizen("Adulto", 18, "0", place, null, false, 60);
        adult.setLastName("Gomez");
        adult.setEducationLevel("College");
        adult.setHomeBuilding(place);
        Citizen senior = citizen("Anziano", 65, "100", null, null, false, 60);
        senior.setEducationLevel("GradeSchool");
        Citizen minor = citizen("Minore", 17, "0", null, null, false, 60);
        minor.setLastName("gOMEZ");
        minor.setEducationLevel("Illiterate");
        flushAndClear();

        assertThat(repository.findByAgeBetween(18, 65)).extracting(Citizen::getId)
                .containsExactlyInAnyOrder(adult.getId(), senior.getId());
        assertThat(repository.findByLastNameContainingIgnoreCase("gOm"))
                .extracting(Citizen::getId).containsExactlyInAnyOrder(adult.getId(), minor.getId());
        assertThat(repository.findByJobBuildingIsNull()).extracting(Citizen::getId)
                .containsExactlyInAnyOrder(senior.getId(), minor.getId());
        assertThat(repository.findByHomeBuildingIsNull()).extracting(Citizen::getId)
                .containsExactlyInAnyOrder(senior.getId(), minor.getId());
        assertThat(repository.findByEducationLevelIn(List.of("College", "GradeSchool")))
                .extracting(Citizen::getId).containsExactlyInAnyOrder(adult.getId(), senior.getId());
    }

    @Test
    void derivedAndJpqlPairsAgreeAndExcludeNullAndBoundaryValues() {
        Faction faction = faction(1, "Fazione test", null);
        Citizen unhappy = citizen("Ribelle infelice", 30, "10", null, faction, true, 20);
        Citizen boundary = citizen("Al limite", 30, "10", null, faction, true, 40);
        citizen("Pacifico", 30, "10", null, null, false, 10);
        citizen("Felicità ignota", 30, "10", null, null, true, null);
        citizen("Ribellione ignota", 30, "10", null, null, null, 10);
        flushAndClear();

        assertThat(repository.findByRebelTrueAndHappinessTotalLessThan(40))
                .extracting(Citizen::getId).containsExactly(unhappy.getId());
        assertThat(repository.findUnhappyRebelsJpql(40))
                .extracting(Citizen::getId).containsExactly(unhappy.getId());
        assertThat(repository.findBySupportedFaction_Name("Fazione test"))
                .extracting(Citizen::getId).containsExactlyInAnyOrder(unhappy.getId(), boundary.getId());
        assertThat(repository.findSupportersJpql("Fazione test"))
                .extracting(Citizen::getId).containsExactlyInAnyOrder(unhappy.getId(), boundary.getId());
        assertThat(repository.findBySupportedFaction_Name("Assente")).isEmpty();
        assertThat(repository.findSupportersJpql("Assente")).isEmpty();
    }

    @Test
    void paginatedJoinsAndExplicitCountUseTheSameFilters() {
        BuildingType industry = buildingType(1, "Fabbrica", "Industry", 100, 4, null, null);
        BuildingType service = buildingType(2, "Servizio", "Service", 100, 4, null, null);
        Building a = building(industry, 3, true);
        Building b = building(industry, 3, true);
        Building other = building(service, 3, true);
        Citizen one = citizen("Uno", 30, "10", a, null, false, 60);
        Citizen two = citizen("Due", 30, "10", a, null, false, 60);
        Citizen three = citizen("Tre", 30, "10", b, null, false, 60);
        citizen("Altra categoria", 30, "10", other, null, false, 60);
        citizen("Disoccupato", 30, "0", null, null, false, 60).setHomeBuilding(a);
        flushAndClear();

        // Tutti hanno lo stesso cognome: id rompe il pareggio. La prima pagina piena
        // obbliga a ottenere il totale; la seconda verifica che non contiamo solo la pagina.
        for (int index = 0; index < 2; index++) {
            var pageable = PageRequest.of(index, 2, Sort.by("lastName").and(Sort.by("id")));
            var derived = repository.findByJobBuilding_BuildingType_Category("Industry", pageable);
            var jpql = repository.findWorkersInCategoryJpql("Industry", pageable);
            List<Integer> expected = index == 0 ? List.of(one.getId(), two.getId()) : List.of(three.getId());
            assertThat(derived.getContent()).extracting(Citizen::getId).containsExactlyElementsOf(expected);
            assertThat(jpql.getContent()).extracting(Citizen::getId).containsExactlyElementsOf(expected);
            assertThat(derived.getTotalElements()).isEqualTo(3);
            assertThat(jpql.getTotalElements()).isEqualTo(3);
            assertThat(jpql.getTotalPages()).isEqualTo(2);
            assertThat(jpql.hasNext()).isEqualTo(index == 0);
        }
        assertThat(repository.findWorkersInCategoryJpql("Government", PageRequest.of(0, 2)).getTotalElements())
                .isZero();
    }

    @Test
    void parenthesesApplyTheAgeRestrictionToBothAlternatives() {
        Citizen adultRebel = citizen("Adulto ribelle", 18, "0", null, null, true, null);
        Citizen adultUnhappy = citizen("Adulto infelice", 30, "0", null, null, false, 20);
        citizen("Minore infelice", 17, "0", null, null, false, 10);
        citizen("Minore ribelle", 17, "0", null, null, true, 80);
        citizen("Adulto al limite", 30, "0", null, null, false, 40);
        flushAndClear();

        assertThat(repository.findAdultsNeedingAttentionJpql(18, 40))
                .extracting(Citizen::getId).containsExactly(adultRebel.getId(), adultUnhappy.getId());
    }

    @Test
    void productionChainRequiresAnActiveJobBuildingAndAnOutputResource() {
        Resource rum = resource(1, "Rum test", "300", true);
        BuildingType distillery = buildingType(1, "Distilleria", "Industry", 100, 4, null, rum);
        BuildingType office = buildingType(2, "Ufficio", "Service", 100, 4, null, null);
        Building active = building(distillery, 3, true);
        Building inactive = building(distillery, 3, false);
        Building noOutput = building(office, 3, true);
        Citizen worker = citizen("Lavora", 30, "10", active, null, false, 60);
        citizen("Edificio inattivo", 30, "10", inactive, null, false, 60);
        citizen("Nessun output", 30, "10", noOutput, null, false, 60);
        citizen("Disoccupato", 30, "0", null, null, false, 60);
        flushAndClear();

        assertThat(repository.findWorkersProducingJpql("Rum test"))
                .extracting(Citizen::getId).containsExactly(worker.getId());
    }

    @Test
    void leftJoinPreservesCitizensWithoutAFaction() {
        Faction faction = faction(1, "Fazione test", null);
        Citizen supporter = citizen("Sostenitore", 30, "10", null, faction, false, 60);
        Citizen independent = citizen("Indipendente", 30, "10", null, null, false, 60);
        flushAndClear();

        assertThat(repository.findCitizenFactionViewsJpql())
                .extracting("citizenId", "firstName", "factionName")
                .containsExactly(tuple(supporter.getId(), "Sostenitore", "Fazione test"),
                        tuple(independent.getId(), "Indipendente", null));
    }

    @Test
    void globalAndWorkplaceAveragesAnswerDifferentQuestions() {
        BuildingType type = buildingType(1, "Fabbrica", "Industry", 100, 4, null, null);
        Building first = building(type, 3, true);
        Building second = building(type, 3, true);
        citizen("Basso", 30, "10", first, null, false, 60);
        Citizen aboveColleagues = citizen("Alto", 30, "30", first, null, false, 60);
        Citizen alone = citizen("Da solo", 30, "100", second, null, false, 60);
        citizen("Salario ignoto", 30, null, first, null, false, 60);
        citizen("Disoccupato", 30, "0", null, null, false, 60);
        flushAndClear();

        // Media globale = 35; primo edificio = 20; secondo edificio = 100.
        assertThat(repository.findEarningAboveAverageJpql()).extracting(Citizen::getId).containsExactly(alone.getId());
        assertThat(repository.findEarningAboveWorkplaceAverageJpql())
                .extracting(Citizen::getId).containsExactly(aboveColleagues.getId());
    }

    @Test
    void groupedReportCountsSupportersButAveragesOnlyKnownHappiness() {
        Faction a = faction(1, "A", null);
        Faction b = faction(2, "B", null);
        Faction c = faction(3, "C", null);
        faction(4, "Vuota", null);
        citizen("Uno", 30, "0", null, a, false, 20);
        citizen("Due", 30, "0", null, a, false, null);
        citizen("Tre", 30, "0", null, b, false, null);
        citizen("Quattro", 30, "0", null, b, false, null);
        citizen("Cinque", 30, "0", null, c, false, 100);
        citizen("Indipendente", 30, "0", null, null, false, 90);
        flushAndClear();

        assertThat(repository.summarizeFactionSupportJpql(2))
                .extracting("factionName", "supporterCount", "averageHappiness")
                .containsExactly(tuple("A", 2L, 20.0), tuple("B", 2L, null));
        assertThat(repository.summarizeFactionSupportJpql(3)).isEmpty();
    }
}
