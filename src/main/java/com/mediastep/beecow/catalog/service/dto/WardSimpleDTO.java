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
 * A DTO for the Ward entity.
 */
@Deprecated
public class WardSimpleDTO implements Serializable {
    private Long id;
    private String code;
    private String inCountry;
    private String outCountry;
    private String zone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WardSimpleDTO wardDTO = (WardSimpleDTO) o;

        if ( ! Objects.equals(id, wardDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WardDTO{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", inCountry='" + inCountry + "'" +
            ", outCountry='" + outCountry + "'" +
            ", zone='" + zone + "'" +
            '}';
    }
}
