package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/** Ordini: date, relazioni facoltative e filtri attraverso più entity. */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // TODO OR1 [Livello 1 - DERIVATA]
    // Trovare gli ordini non ancora spediti: shippedDate null. Restituire List<Order>.
    List<Order> findByShippedDateIsNull();

    // TODO OR2 [Livello 2 - DERIVATA]
    // Dati LocalDateTime start e end, trovare gli ordini con orderDate nell'intervallo
    // [start, end): start INCLUSO, end ESCLUSO. Restituire List<Order> per orderDate e id.
    // Qui Between non rappresenta i confini richiesti. Per un giorno, end è la
    // mezzanotte del giorno successivo, non le 23:59:59 del giorno iniziale.
    List<Order> findByOrderDateGreaterThanEqualAndOrderDateLessThanOrderByOrderDateAscIdAsc(LocalDateTime start, LocalDateTime end);

    // TODO OR3 [Livello 3 - DERIVATA + JPQL, DUE METODI]
    // Dato String country, trovare gli ordini di clienti residenti in quel paese.
    // Restituire List<Order>. Usare customer.country, NON shipCountry (destinazione).
    // Gli ordini senza cliente non devono corrispondere.

    // TODO OR4 [Livello 3 - JPQL, CONFRONTO FRA ATTRIBUTI]
    // Trovare gli ordini spediti DOPO la loro requiredDate: List<Order> per id.
    // Escludere ordini non spediti e ordini spediti esattamente alla data richiesta.
    // Non serve una data in ingresso: confrontare i due attributi dello stesso ordine.

    // TODO OR5 [Livello 4 - DERIVATA, SLICE]
    // Dati String shipCountry e Pageable, restituire Slice<Order> degli ordini spediti
    // verso quel paese (qui si filtra la destinazione, non il paese del cliente).
    // Includere anche ordini in attesa di spedizione. Ordinare dal chiamante per id.
    // Spiegare perché Slice offre hasNext(), ma non getTotalElements().

    // TODO OR6 [Livello 4 - JPQL, PAGE E PIÙ RELAZIONI]
    // Dati String country, Integer managerId e Pageable, cercare gli ordini il cui
    // cliente vive nel paese richiesto E il cui employee ha quel manager diretto.
    // Restituire Page<Order>; escludere cliente o manager assenti. Ordinare per id
    // tramite Pageable. Questo percorso è più lungo, ma non implica GROUP BY.

    // TODO OR7 [Livello 5 - JPQL, SOTTOQUERY CORRELATA]
    // Trovare gli ordini con freight maggiore della media degli ordini dello STESSO
    // cliente. Restituire List<Order> per id. Escludere gli ordini senza cliente.
    // Se il cliente ha un solo ordine, quell'ordine non supera la propria media.
}
