package com.mediastep.beecow.catalog.web.rest.vm;

import com.mediastep.beecow.common.dto.CountryDTO;
import lombok.Data;
import lombok.ToString;

/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 30/10/2021
 * Author: Quy Luong <quy.luong@mediastep.com>
 *******************************************************************************/
@Data
@ToString
public class FullAddressVM {
    private String address1;
    private String address2;
    private String wardCode;
    private String districtCode;
    private String countryCode;
    private String locationCode;
    private String city;
    private String zipCode;
}
