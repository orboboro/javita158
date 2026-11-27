package org.generation.italy.javita158.config;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Mantiene il dominio Tropico per i profili esistenti; company lo esclude. */
@Configuration(proxyBeanMethods = false)
@Profile("!company")
@EntityScan(basePackageClasses = Resource.class)
@EnableJpaRepositories(basePackageClasses = ResourceRepository.class)
public class TropicoJpaConfiguration {
}
