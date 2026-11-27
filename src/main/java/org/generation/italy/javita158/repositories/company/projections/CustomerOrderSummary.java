package org.generation.italy.javita158.repositories.company.projections;

/** DTO del laboratorio: usare SELECT NEW con il nome completo della classe.
 * Il costruttore riceve i valori nello stesso ordine della SELECT; non è un’entity. */
public final class CustomerOrderSummary {

    private final Integer customerId;
    private final String companyName;
    private final Long orderCount;

    public CustomerOrderSummary(Integer customerId, String companyName, Long orderCount) {
        this.customerId = customerId;
        this.companyName = companyName;
        this.orderCount = orderCount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Long getOrderCount() {
        return orderCount;
    }
}
