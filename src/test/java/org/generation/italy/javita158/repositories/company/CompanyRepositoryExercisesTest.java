package org.generation.italy.javita158.repositories.company;

import jakarta.persistence.EntityManager;
import org.generation.italy.javita158.controllers.tropico.ResourceController;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;
import org.generation.italy.javita158.services.tropico.TropicoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica l'infrastruttura del laboratorio sul company esistente, senza seed o scritture.
 * Non contiene le soluzioni dei TODO: gli studenti aggiungeranno i test delle loro query.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("company")
@Transactional(readOnly = true)
class CompanyRepositoryExercisesTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EntityManager entityManager;

    @Test
    void companyProfileManagesOnlyCompanyEntities() {
        assertThat(entityManager.getMetamodel().getEntities())
                .extracting(entity -> entity.getJavaType().getSimpleName())
                .containsExactlyInAnyOrder("Category", "Customer", "Employee", "Order",
                        "OrderDetail", "Product", "Shipper", "Supplier");
    }

    @Test
    void companyProfileRegistersItsEightRepositoriesWithoutTropicoComponents() {
        assertThat(context.getBeansOfType(JpaRepository.class)).hasSize(8);
        assertThat(context.getBeansOfType(ResourceRepository.class)).isEmpty();
        assertThat(context.getBeansOfType(TropicoService.class)).isEmpty();
        assertThat(context.getBeansOfType(ResourceController.class)).isEmpty();
    }

    @Test
    void everyEmptyRepositoryAlreadySupportsInheritedReadOperations() {
        for (JpaRepository<?, ?> repository : context.getBeansOfType(JpaRepository.class).values()) {
            assertThat(repository.count()).isNotNegative();
            // Carica al massimo una riga per verificare anche i tipi dei campi mappati.
            assertThat(repository.findAll(PageRequest.of(0, 1)).getContent()).hasSizeLessThanOrEqualTo(1);
        }
    }
}
