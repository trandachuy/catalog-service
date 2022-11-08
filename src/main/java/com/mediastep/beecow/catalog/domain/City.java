package com.mediastep.beecow.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A City.
 */
@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class City implements Serializable {

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

    @Column(name = "is_show")
    private Boolean isShow;

    @Column(name = "order_number")
    private Integer orderNumber;

    @ManyToOne
    private Country country;
    
    @OneToMany(mappedBy = "city")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<District> districts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public City code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInCountry() {
        return inCountry;
    }

    public City inCountry(String inCountry) {
        this.inCountry = inCountry;
        return this;
    }

    public void setInCountry(String inCountry) {
        this.inCountry = inCountry;
    }

    public String getOutCountry() {
        return outCountry;
    }

    public City outCountry(String outCountry) {
        this.outCountry = outCountry;
        return this;
    }

    public void setOutCountry(String outCountry) {
        this.outCountry = outCountry;
    }

    public Country getCountry() {
        return country;
    }

    public City country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Set<District> getDistricts() {
		return districts;
	}

	public void setDistricts(Set<District> districts) {
		this.districts = districts;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        City city = (City) o;
        if (city.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, city.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "City{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", inCountry='" + inCountry + "'" +
            ", outCountry='" + outCountry + "'" +
            '}';
    }
}
