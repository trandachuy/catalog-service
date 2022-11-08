/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 18/7/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Shipper entity.
 */
public class SimpleShipperDTO implements Serializable {

    private Long id;

    private String inCountry;

    private String outCountry;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimpleShipperDTO shipperDTO = (SimpleShipperDTO) o;

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
