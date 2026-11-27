package org.generation.italy.javita158.config;

import org.generation.italy.javita158.model.company.Customer;
import org.generation.italy.javita158.repositories.company.CustomerRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Un solo database per esecuzione: questo profilo registra solo il dominio company. */
@Configuration(proxyBeanMethods = false)
@Profile("company")
@EntityScan(basePackageClasses = Customer.class)
@EnableJpaRepositories(basePackageClasses = CustomerRepository.class)
public class CompanyJpaConfiguration {
}
