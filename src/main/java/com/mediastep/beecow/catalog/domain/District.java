package com.mediastep.beecow.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A District.
 */
@Entity
@Table(name = "district")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "in_country")
    private String inCountry;

    @Column(name = "out_country")
    private String outCountry;

    @Column(name = "zone")
    private String zone;

    @ManyToOne
    @JsonIgnoreProperties("")
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public District code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInCountry() {
        return inCountry;
    }

    public District inCountry(String inCountry) {
        this.inCountry = inCountry;
        return this;
    }

    public void setInCountry(String inCountry) {
        this.inCountry = inCountry;
    }

    public String getOutCountry() {
        return outCountry;
    }

    public District outCountry(String outCountry) {
        this.outCountry = outCountry;
        return this;
    }

    public void setOutCountry(String outCountry) {
        this.outCountry = outCountry;
    }

    public String getZone() {
        return zone;
    }

    public District zone(String zone) {
        this.zone = zone;
        return this;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public City getCity() {
        return city;
    }

    public District city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        District district = (District) o;
        if (district.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), district.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", inCountry='" + getInCountry() + "'" +
            ", outCountry='" + getOutCountry() + "'" +
            ", zone='" + getZone() + "'" +
            "}";
    }
}
