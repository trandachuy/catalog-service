package com.mediastep.beecow.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Ward.
 */
@Entity
@Table(name = "ward")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ward implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties("")
    private District district;

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

    public Ward code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInCountry() {
        return inCountry;
    }

    public Ward inCountry(String inCountry) {
        this.inCountry = inCountry;
        return this;
    }

    public void setInCountry(String inCountry) {
        this.inCountry = inCountry;
    }

    public String getOutCountry() {
        return outCountry;
    }

    public Ward outCountry(String outCountry) {
        this.outCountry = outCountry;
        return this;
    }

    public void setOutCountry(String outCountry) {
        this.outCountry = outCountry;
    }

    public District getDistrict() {
        return district;
    }

    public Ward district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
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
        Ward ward = (Ward) o;
        if (ward.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ward.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ward{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", inCountry='" + getInCountry() + "'" +
            ", outCountry='" + getOutCountry() + "'" +
            "}";
    }
}
