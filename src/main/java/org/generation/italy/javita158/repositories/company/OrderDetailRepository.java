package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.OrderDetail;
import org.generation.italy.javita158.model.company.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/** Una riga d'ordine ha chiave composta (orderId, productId), non un Integer. */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

    // TODO OD1 [Livello 2 - DERIVATA]
    // Dato Integer minimum, trovare i dettagli con quantity >= minimum.
    // Restituire List<OrderDetail>. L'attributo Java è quantity, la colonna SQL è qty.
    List<OrderDetail> findByQuantityGreaterThanEqual(Integer minimum);

    // TODO OD2 [Livello 3 - DERIVATA]
    // Dato Integer orderId, restituire tutti i dettagli di quell'ordine: List<OrderDetail>.
    // Attraversare order.id. Non confondere l'id dell'ordine con l'intera chiave del dettaglio.
    List<OrderDetail> findByOrderId(Integer orderId);

    // TODO OD3 [Livello 3 - JPQL, PIÙ JOIN]
    // Dato String categoryName, trovare i dettagli che contengono prodotti della
    // categoria richiesta. Restituire List<OrderDetail>, ordinata per order.id e product.id.
    // Seguire product.category; non esiste un attributo category su OrderDetail.
    @Query("""
            SELECT od
            FROM OrderDetail od
            JOIN od.product p
            JOIN p.category c
            WHERE c.categoryName = :categoryName
            ORDER BY od.order.id ASC, p.id ASC
""")
    List<OrderDetail> findByCategoryName(
            @Param("categoryName") String categoryName
    );

    // TODO OD4 [Livello 5 - JPQL, AGGREGAZIONE SCALARE]
    // Dato Integer orderId, calcolare il totale dell'ordine come BigDecimal:
    // somma di unitPrice * quantity * (1 - discount) dei suoi dettagli.
    // Usare il prezzo STORICO OrderDetail.unitPrice, non il prezzo corrente del Product.
    // Usare COALESCE per restituire zero se non esistono dettagli per quell'id.

    // TODO OD5 [Livello 5 - JPQL, GROUP BY/HAVING + DTO]
    // Dato long minimumQuantity >= 1, riportare i prodotti la cui quantità totale
    // ordinata è almeno N. Restituire List<ProductSalesSummary>: productId,
    // productName, totalQuantity (Long: SUM di un Integer). Escludere prodotti mai ordinati.
    // Usare SELECT NEW; ordinare per quantità totale decrescente, poi productId.
    // SUM(quantity) conta pezzi; COUNT conta righe: non sono la stessa cosa.
}
