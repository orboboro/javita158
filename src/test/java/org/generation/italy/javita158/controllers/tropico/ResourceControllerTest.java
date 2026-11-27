package org.generation.italy.javita158.controllers.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.services.tropico.TropicoService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ResourceControllerTest {

    @Test
    void listResourcesUsesTheInjectedService() throws Exception {
        TropicoService service = mock(TropicoService.class);
        when(service.getResources()).thenReturn(List.of(
                new Resource(1, "steel", BigDecimal.TEN, true)));
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new ResourceController(service)).build();

        mvc.perform(get("/api/resource"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("steel"))
                .andExpect(jsonPath("$[0].processed").value("PROCESSED"));

        verify(service).getResources();
    }
}
