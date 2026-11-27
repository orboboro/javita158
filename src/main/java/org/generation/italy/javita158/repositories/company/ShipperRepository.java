package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Esercizi brevi sui corrieri. */
public interface ShipperRepository extends JpaRepository<Shipper, Integer> {

    // TODO SH1 [Livello 1 - DERIVATA]
    // Dato String name, restituire List<Shipper> con companyName esattamente uguale.

    // TODO SH2 [Livello 2 - DERIVATA]
    // Dato String prefix, cercare i corrieri il cui companyName INIZIA con quel testo.
    // Ignorare il caso; restituire List<Shipper>, ordinata per companyName e poi id.
	List<Shipper> findByCompanyNameStartingWithIgnoreCaseOrderByCompanyNameId(String prefix);

    // TODO SH3 [Livello 5 - JPQL, NOT EXISTS]
    // Trovare i corrieri mai utilizzati da un ordine: List<Shipper> in ordine di id.
    // Shipper non contiene una collezione orders; la relazione è Order.shipper.
}
