package com.mediastep.beecow.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Shipper.
 */
@Entity
@Table(name = "shipper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Shipper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "in_country")
    private String inCountry;

    @Column(name = "out_country")
    private String outCountry;

    @ManyToOne
    private Country country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInCountry() {
        return inCountry;
    }

    public Shipper inCountry(String inCountry) {
        this.inCountry = inCountry;
        return this;
    }

    public void setInCountry(String inCountry) {
        this.inCountry = inCountry;
    }

    public String getOutCountry() {
        return outCountry;
    }

    public Shipper outCountry(String outCountry) {
        this.outCountry = outCountry;
        return this;
    }

    public void setOutCountry(String outCountry) {
        this.outCountry = outCountry;
    }

    public Country getCountry() {
        return country;
    }

    public Shipper country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shipper shipper = (Shipper) o;
        if (shipper.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shipper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Shipper{" +
            "id=" + id +
            ", inCountry='" + inCountry + "'" +
            ", outCountry='" + outCountry + "'" +
            '}';
    }
}
