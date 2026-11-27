package org.generation.italy.javita158;

import org.junit.jupiter.api.Test;
import org.generation.italy.javita158.services.tropico.JpaTropicoService;
import org.generation.italy.javita158.services.tropico.TropicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

// Anche il test di avvio usa tropico_test, nello stesso container della demo.
@SpringBootTest
@ActiveProfiles({"test", "jpa"})
class Javita158ApplicationTests {

	@Autowired
	private TropicoService service;

	@Test
	void contextLoads() {
		assertThat(service).isInstanceOf(JpaTropicoService.class);
	}

}
