package org.generation.italy.javita158.model.tropico;


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

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

/** Modello della tabella citizen, mappato per Hibernate. */
@Entity
@Table(name = "citizen", schema = "public")
public class Citizen {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "citizen_id_generator")
    @SequenceGenerator(name = "citizen_id_generator", sequenceName = "citizen_id_seq",
            schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "education_level", length = 20)
    private String educationLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_building_id")
    private Building jobBuilding;

    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_building_id")
    private Building homeBuilding;

    @Column(name = "wealth_level", length = 20)
    private String wealthLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supported_faction_id")
    private Faction supportedFaction;

    @Column(name = "is_rebel")
    private Boolean rebel = false;

    @Column(name = "happiness_total")
    private Integer happinessTotal;

    public Citizen() {
    }

    public Citizen(
            Integer id,
            String firstName,
            String lastName,
            String gender,
            Integer age,
            String educationLevel,
            Building jobBuilding,
            BigDecimal salary,
            Building homeBuilding,
            String wealthLevel,
            Faction supportedFaction,
            Boolean rebel,
            Integer happinessTotal) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.educationLevel = educationLevel;
        this.jobBuilding = jobBuilding;
        this.salary = salary;
        this.homeBuilding = homeBuilding;
        this.wealthLevel = wealthLevel;
        this.supportedFaction = supportedFaction;
        this.rebel = rebel;
        this.happinessTotal = happinessTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Building getJobBuilding() {
        return jobBuilding;
    }

    public void setJobBuilding(Building jobBuilding) {
        this.jobBuilding = jobBuilding;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Building getHomeBuilding() {
        return homeBuilding;
    }

    public void setHomeBuilding(Building homeBuilding) {
        this.homeBuilding = homeBuilding;
    }

    public String getWealthLevel() {
        return wealthLevel;
    }

    public void setWealthLevel(String wealthLevel) {
        this.wealthLevel = wealthLevel;
    }

    public Faction getSupportedFaction() {
        return supportedFaction;
    }

    public void setSupportedFaction(Faction supportedFaction) {
        this.supportedFaction = supportedFaction;
    }

    public Boolean getRebel() {
        return rebel;
    }

    public void setRebel(Boolean rebel) {
        this.rebel = rebel;
    }

    public Integer getHappinessTotal() {
        return happinessTotal;
    }

    public void setHappinessTotal(Integer happinessTotal) {
        this.happinessTotal = happinessTotal;
    }
}

