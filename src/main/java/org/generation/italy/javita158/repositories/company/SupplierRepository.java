package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/** Esercizi sui fornitori. */
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    // TODO SU1 [Livello 1 - DERIVATA]
    // Dato String city, restituire List<Supplier> dei fornitori di quella città.
	List<Supplier> findByCity (String city);

    // TODO SU2 [Livello 2 - DERIVATA]
    // Data Collection<String> countries non vuota, cercare i fornitori di uno dei paesi.
    // Restituire List<Supplier>. Non passare una stringa contenente paesi separati da virgole.
	List<Supplier> findByCountryIn (Collection<String> countries);

    // TODO SU3 [Livello 2 - DERIVATA]
    // Dato String country, restituire long: quanti fornitori sono di quel paese?
    // Usare una query di conteggio; non caricare la lista per chiamare size().
	long countByCountry(String country);

    // TODO SU4 [Livello 3 - JPQL]
    // Riscrivere SU2 con IN e un parametro nominato; restituire List<Supplier>.
    // Confrontare gli insiemi di id, senza presumere un ordine implicito.

    // TODO SU5 [Livello 5 - JPQL, EXISTS]
    // Dato BigDecimal threshold, trovare i fornitori con almeno un prodotto
    // non ritirato (discontinued = 0) e con unitPrice strettamente maggiore della soglia.
    // Restituire List<Supplier>, ogni fornitore una sola volta, in ordine di id.
}
