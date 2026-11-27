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

import java.time.LocalDateTime;

/** Entity della tabella employees del database company. */
@Entity
@Table(name = "employees", schema = "public")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employees_id_generator")
    @SequenceGenerator(name = "employees_id_generator", sequenceName = "employees_empid_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "empid", nullable = false)
    private Integer id;

    @Column(name = "lastname", nullable = false, length = 20)
    private String lastName;

    @Column(name = "firstname", nullable = false, length = 10)
    private String firstName;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "titleofcourtesy", nullable = false, length = 25)
    private String titleOfCourtesy;

    @Column(name = "birthdate", nullable = false)
    private LocalDateTime birthDate;

    @Column(name = "hiredate", nullable = false)
    private LocalDateTime hireDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mgrid")
    private Employee manager;

    public Employee() {
    }

    public Employee(
            Integer id,
            String lastName,
            String firstName,
            String title,
            String titleOfCourtesy,
            LocalDateTime birthDate,
            LocalDateTime hireDate,
            String address,
            String city,
            String region,
            String postalCode,
            String country,
            String phone,
            Employee manager) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.title = title;
        this.titleOfCourtesy = titleOfCourtesy;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.manager = manager;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleOfCourtesy() {
        return titleOfCourtesy;
    }

    public void setTitleOfCourtesy(String titleOfCourtesy) {
        this.titleOfCourtesy = titleOfCourtesy;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
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

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
