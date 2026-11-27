package org.generation.italy.javita158.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** Entity della tabella customers del database company. */
@Entity
@Table(name = "customers", schema = "public")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_id_generator")
    @SequenceGenerator(name = "customers_id_generator", sequenceName = "customers_custid_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "custid", nullable = false)
    private Integer id;

    @Column(name = "companyname", nullable = false, length = 40)
    private String companyName;

    @Column(name = "contactname", nullable = false, length = 30)
    private String contactName;

    @Column(name = "contacttitle", nullable = false, length = 30)
    private String contactTitle;

    @Column(name = "address", nullable = false, length = 60)
    private String address;

    @Column(name = "city", nullable = false, length = 15)
    private String city;

    @Column(name = "region", length = 15)
    private String region;

    @Column(name = "postalcode", length = 10)
    private String postalCode;

    @Column(name = "country", nullable = false, length = 15)
    private String country;

    @Column(name = "phone", nullable = false, length = 24)
    private String phone;

    @Column(name = "fax", length = 24)
    private String fax;

    // "customer" è il nome del campo Java in Order che gestisce la chiave esterna.
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(
            Integer id,
            String companyName,
            String contactName,
            String contactTitle,
            String address,
            String city,
            String region,
            String postalCode,
            String country,
            String phone,
            String fax) {
        this.id = id;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public List<Order> getOrders() {
        return orders;
    }

    // Usiamo questi metodi invece di modificare direttamente la lista.
    public void addOrder(Order order) {
        order.setCustomer(this);
    }

    public void removeOrder(Order order) {
        if (orders.remove(order)) {
            order.setCustomer(null);
        }
    }
}
