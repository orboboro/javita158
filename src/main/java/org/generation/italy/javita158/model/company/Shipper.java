package org.generation.italy.javita158.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** Entity della tabella shippers del database company. */
@Entity
@Table(name = "shippers", schema = "public")
public class Shipper {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shippers_id_generator")
    @SequenceGenerator(name = "shippers_id_generator", sequenceName = "shippers_shipperid_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "shipperid", nullable = false)
    private Integer id;

    @Column(name = "companyname", nullable = false, length = 40)
    private String companyName;

    @Column(name = "phone", nullable = false, length = 24)
    private String phone;

    public Shipper() {
    }

    public Shipper(
            Integer id,
            String companyName,
            String phone) {
        this.id = id;
        this.companyName = companyName;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
