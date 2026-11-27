package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JpaTropicoServiceTest {

    private final ResourceRepository repository = mock(ResourceRepository.class);
    private final JpaTropicoService service = new JpaTropicoService(repository);

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = 99)
    void updateKeepsThePathIdWhenTheBodyIdIsMissingOrDifferent(Integer bodyId) {
        Resource existing = new Resource(1, "coal", BigDecimal.ONE, false);
        Resource incoming = new Resource(bodyId, "steel", BigDecimal.TEN, true);
        when(repository.findById(1)).thenReturn(Optional.of(existing));

        assertTrue(service.update(1, incoming));

        verify(repository).save(same(existing));
        assertEquals(Integer.valueOf(1), existing.getId());
        assertEquals("steel", existing.getName());
        assertEquals(BigDecimal.TEN, existing.getBaseMarketPrice());
        assertEquals(Boolean.TRUE, existing.getProcessed());
    }

    @Test
    void updateDoesNotSaveWhenThePathIdDoesNotExist() {
        when(repository.findById(42)).thenReturn(Optional.empty());

        assertFalse(service.update(42,
                new Resource(1, "steel", BigDecimal.TEN, true)));

        verify(repository, never()).save(any(Resource.class));
    }
}
