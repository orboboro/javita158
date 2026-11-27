package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/** Esercizi sui clienti: dai filtri a un report sugli ordini. */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // TODO CU1 [Livello 1 - DERIVATA]
    // Dato String country, restituire List<Customer> dei clienti di quel paese.
    // Il confronto è esatto; non è richiesta una conversione maiuscole/minuscole.
    List<Customer> findByCountry(String country);

    // TODO CU2 [Livello 1 - DERIVATA]
    // Restituire List<Customer> dei clienti con region null. Nessun parametro.
    // Una stringa vuota non equivale a null.
    List<Customer> findByRegionIsNull();

    // TODO CU3 [Livello 2 - DERIVATA]
    // Dati String country e String city, restituire i clienti che soddisfano ENTRAMBI.
    // Restituire List<Customer>, ordinata per companyName crescente, poi id crescente.
    List<Customer> findByCountryAndCityOrderByCompanyNameAscIdAsc(String country, String city);


    // TODO CU4 [Livello 2 - DERIVATA]
    // Dato String text, cercare companyName che CONTIENE il testo, ignorando il caso.
    // Restituire List<Customer>. Provare la stessa parola con maiuscole diverse.
    List<Customer> findByCompanyNameContainingIgnoreCase(String text);

    // TODO CU5 [Livello 3 - JPQL]
    // Riscrivere CU3 con @Query, parametri nominati e lo stesso ordinamento.
    // Confrontare la sequenza degli id restituiti dalle due versioni.
    @Query("""
            SELECT c
            FROM Customer c 
            WHERE c.country = :country
                       AND c.city = :city        
            ORDER BY c.companyName ASC, c.id ASC
           """)
    List<Customer> findCustomerByCountryAndCity(
            @Param("country") String country,
            @Param("city") String city
    );

    // TODO CU6 [Livello 4 - DERIVATA, PAGE]
    // Come CU1, ma restituire Page<Customer> e accettare anche Pageable.
    // Dal chiamante richiedere pagine di 5, ordinate per companyName e poi id.
    // Controllare contenuto, totale, numero di pagine e una pagina oltre la fine.

    // TODO CU7 [Livello 5 - JPQL, NOT EXISTS]
    // Restituire List<Customer> dei clienti che non hanno mai fatto ordini, per id.
    // La sottoquery deve correlare Order.customer al cliente della query esterna.

    // TODO CU8 [Livello 5 - JPQL, GROUP BY/HAVING + DTO]
    // Dato long minimumOrders >= 1, produrre una riga per cliente con almeno N ordini.
    // Restituire List<CustomerOrderSummary>: customerId, companyName, orderCount.
    // Usare il DTO già fornito e SELECT NEW; raggruppare per id e nome del cliente.
    // Escludere ordini senza cliente; ordinare per conteggio decrescente, poi id.
    // Due clienti con lo stesso nome devono restare due gruppi distinti.
}
