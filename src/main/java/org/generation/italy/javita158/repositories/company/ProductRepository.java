package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/** Prezzi, relazioni, paginazione e report. discontinued è Integer (0/1), non Boolean. */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // TODO PR1 [Livello 1 - DERIVATA]
    // Dato BigDecimal threshold, trovare i prodotti con unitPrice strettamente minore.
    // Restituire List<Product>. Un prodotto esattamente sulla soglia va escluso.
    List<Product> findByUnitPriceLessThan(BigDecimal threshold);

    // TODO PR2 [Livello 1 - DERIVATA]
    // Dato Integer status, restituire List<Product> con quel valore di discontinued.
    // Provare 0 (in vendita) e 1 (ritirato). Non usare True/False nel nome del metodo.
    List<Product>  findByDiscontinued(Integer status);

    // TODO PR3 [Livello 2 - DERIVATA]
    // Dati BigDecimal minimum e maximum, cercare unitPrice fra gli estremi INCLUSI.
    // Restituire List<Product>, ordinata per unitPrice crescente e poi id crescente.
    List<Product> findByunitPriceBetweenOrderByUnitPriceAscIdAsc(BigDecimal minimum, BigDecimal maximum);

    // TODO PR4 [Livello 3 - DERIVATA + JPQL, DUE METODI]
    // Dato String categoryName, trovare i prodotti della categoria con quel nome.
    // Restituire List<Product>. Prima attraversare category.categoryName nel nome
    // del metodo, poi riscrivere la ricerca con un JOIN in JPQL. Confrontare gli id.

    // TODO PR5 [Livello 3 - JPQL, PARENTESI]
    // Dati BigDecimal threshold e String country, trovare i prodotti per cui:
    // discontinued = 0 E (unitPrice < threshold O supplier.country = country).
    // Restituire List<Product>, ordinata per id. Un prodotto ritirato va sempre escluso,
    // anche se il suo fornitore è del paese richiesto. Riflettere sulla precedenza AND/OR.

    // TODO PR6 [Livello 4 - JPQL, PAGE]
    // Come PR4, ma Page<Product> con String categoryName e Pageable.
    // Usare @Query senza countQuery: per questo filtro Spring sa ottenere il totale.
    // Dal chiamante ordinare per unitPrice e poi id; verificare una prima pagina piena.
    // Facoltativo: scrivere anche countQuery e confrontare i totali delle due versioni.

    // TODO PR7 [Livello 4 - JPQL, JOIN FETCH]
    // Dato Integer id, restituire Optional<Product> caricando anche category e supplier
    // nella stessa SELECT. Restituire Optional.empty() se l'id non esiste.
    // Usare FETCH sulle due relazioni ManyToOne; osservare il log SQL.

    // TODO PR8 [Livello 5 - JPQL, SOTTOQUERY]
    // Restituire List<Product> con unitPrice maggiore della media di TUTTI i prodotti,
    // inclusi quelli ritirati. Ordinare per prezzo decrescente, poi id crescente.

    // TODO PR9 [Livello 5 - JPQL, GROUP BY/HAVING + DTO]
    // Dato long minimumProducts >= 1, produrre una riga per categoria con almeno N prodotti.
    // Restituire List<CategoryProductSummary>: categoryId, categoryName, productCount,
    // averagePrice. Includere prodotti ritirati; escludere categorie senza prodotti.
    // Usare SELECT NEW con la classe fornita; ordinare per categoryId crescente.
}
