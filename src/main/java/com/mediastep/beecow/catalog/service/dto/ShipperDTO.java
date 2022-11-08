package com.mediastep.beecow.catalog.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Shipper entity.
 */
public class ShipperDTO implements Serializable {

    private Long id;

    private String inCountry;

    private String outCountry;


    private Long countryId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getInCountry() {
        return inCountry;
    }

    public void setInCountry(String inCountry) {
        this.inCountry = inCountry;
    }
    public String getOutCountry() {
        return outCountry;
    }

    public void setOutCountry(String outCountry) {
        this.outCountry = outCountry;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShipperDTO shipperDTO = (ShipperDTO) o;

        if ( ! Objects.equals(id, shipperDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShipperDTO{" +
            "id=" + id +
            ", inCountry='" + inCountry + "'" +
            ", outCountry='" + outCountry + "'" +
            '}';
    }
}
