package org.generation.italy.javita158.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Entity della tabella orders del database company. */
@Entity
@Table(name = "orders", schema = "public")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_generator")
    @SequenceGenerator(name = "orders_id_generator", sequenceName = "orders_orderid_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "orderid", nullable = false)
    private Integer id;

    // Lato proprietario: questa relazione determina il valore della colonna custid.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custid")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empid", nullable = false)
    private Employee employee;

    @Column(name = "orderdate", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "requireddate", nullable = false)
    private LocalDateTime requiredDate;

    @Column(name = "shippeddate")
    private LocalDateTime shippedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipperid", nullable = false)
    private Shipper shipper;

    @Column(name = "freight", nullable = false, columnDefinition = "numeric")
    private BigDecimal freight = BigDecimal.ZERO;

    @Column(name = "shipname", nullable = false, length = 40)
    private String shipName;

    @Column(name = "shipaddress", nullable = false, length = 60)
    private String shipAddress;

    @Column(name = "shipcity", nullable = false, length = 15)
    private String shipCity;

    @Column(name = "shipregion", length = 15)
    private String shipRegion;

    @Column(name = "shippostalcode", length = 10)
    private String shipPostalCode;

    @Column(name = "shipcountry", nullable = false, length = 15)
    private String shipCountry;

    public Order() {
    }

    public Order(
            Integer id,
            Customer customer,
            Employee employee,
            LocalDateTime orderDate,
            LocalDateTime requiredDate,
            LocalDateTime shippedDate,
            Shipper shipper,
            BigDecimal freight,
            String shipName,
            String shipAddress,
            String shipCity,
            String shipRegion,
            String shipPostalCode,
            String shipCountry) {
        this.id = id;
        setCustomer(customer);
        this.employee = employee;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.shipper = shipper;
        this.freight = freight;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.shipRegion = shipRegion;
        this.shipPostalCode = shipPostalCode;
        this.shipCountry = shipCountry;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer == customer) {
            return;
        }
        // Manteniamo coerenti entrambi i lati, anche quando cambia il cliente.
        if (this.customer != null) {
            this.customer.getOrders().remove(this);
        }
        this.customer = customer;
        if (customer != null && !customer.getOrders().contains(this)) {
            customer.getOrders().add(this);
        }
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(LocalDateTime requiredDate) {
        this.requiredDate = requiredDate;
    }

    public LocalDateTime getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDateTime shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipRegion() {
        return shipRegion;
    }

    public void setShipRegion(String shipRegion) {
        this.shipRegion = shipRegion;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }
}
