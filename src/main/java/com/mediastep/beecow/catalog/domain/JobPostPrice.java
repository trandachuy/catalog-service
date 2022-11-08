package com.mediastep.beecow.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JobPostPrice.
 */
@Entity
@Table(name = "job_post_price")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobPostPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_of_cv")
    private Long numberOfCV;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "price")
    private Long price;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "package_name")
    private String packageName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberOfCV() {
        return numberOfCV;
    }

    public JobPostPrice numberOfCV(Long numberOfCV) {
        this.numberOfCV = numberOfCV;
        return this;
    }

    public void setNumberOfCV(Long numberOfCV) {
        this.numberOfCV = numberOfCV;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public JobPostPrice countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Long getPrice() {
        return price;
    }

    public JobPostPrice price(Long price) {
        this.price = price;
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public JobPostPrice currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobPostPrice jobPostPrice = (JobPostPrice) o;
        if (jobPostPrice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jobPostPrice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobPostPrice{" +
            "id=" + id +
            ", numberOfCV='" + numberOfCV + "'" +
            ", countryCode='" + countryCode + "'" +
            ", price='" + price + "'" +
            ", currencyCode='" + currencyCode + "'" +
            '}';
    }
}
