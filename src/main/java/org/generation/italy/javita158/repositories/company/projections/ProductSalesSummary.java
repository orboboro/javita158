package org.generation.italy.javita158.repositories.company.projections;

/** DTO del laboratorio: usare SELECT NEW con il nome completo della classe.
 * Il costruttore riceve i valori nello stesso ordine della SELECT; non è un’entity. */
public final class ProductSalesSummary {

    private final Integer productId;
    private final String productName;
    private final Long totalQuantity;

    public ProductSalesSummary(Integer productId, String productName, Long totalQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.totalQuantity = totalQuantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }
}
