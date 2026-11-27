package org.generation.italy.javita158.repositories.company.projections;

/** DTO del laboratorio: usare SELECT NEW con il nome completo della classe.
 * Il costruttore riceve i valori nello stesso ordine della SELECT; non è un’entity. */
public final class CategoryProductSummary {

    private final Integer categoryId;
    private final String categoryName;
    private final Long productCount;
    private final Double averagePrice;

    public CategoryProductSummary(Integer categoryId, String categoryName, Long productCount, Double averagePrice) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productCount = productCount;
        this.averagePrice = averagePrice;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getProductCount() {
        return productCount;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }
}
