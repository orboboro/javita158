package org.generation.italy.javita158.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** Entity della tabella categories del database company. */
@Entity
@Table(name = "categories", schema = "public")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_id_generator")
    @SequenceGenerator(name = "categories_id_generator", sequenceName = "categories_categoryid_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "categoryid", nullable = false)
    private Integer id;

    @Column(name = "categoryname", nullable = false, length = 15)
    private String categoryName;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    public Category() {
    }

    public Category(
            Integer id,
            String categoryName,
            String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
