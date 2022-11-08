package com.mediastep.beecow.catalog.service.dto;


import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import com.mediastep.beecow.common.dto.CountryDTO;

/**
 * A DTO for the JobPostPrice entity.
 */
public class JobPostPriceDTO implements Serializable {

    private Long id;

    private Long numberOfCV;

    private String countryCode;

    private Long price;

    private String currencyCode;
    
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

    public void setNumberOfCV(Long numberOfCV) {
        this.numberOfCV = numberOfCV;
    }
    
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
    public String getCurrencyCode() {
        return currencyCode;
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

        JobPostPriceDTO jobPostPriceDTO = (JobPostPriceDTO) o;

        if ( ! Objects.equals(id, jobPostPriceDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobPostPriceDTO{" +
            "id=" + id +
            ", numberOfCV='" + numberOfCV + "'" +
            ", countryCode='" + countryCode + "'" +
            ", price='" + price + "'" +
            ", currencyCode='" + currencyCode + "'" +
            ", packageName='" + packageName + "'" +
            '}';
    }
    
    public static Comparator<JobPostPriceDTO> numberOfCVComparator = new Comparator<JobPostPriceDTO>() {
    	public int compare(JobPostPriceDTO jobPostPrice1, JobPostPriceDTO jobPostPrice2) {
			return jobPostPrice1.getNumberOfCV().compareTo(jobPostPrice2.getNumberOfCV());
		}
    };
}
