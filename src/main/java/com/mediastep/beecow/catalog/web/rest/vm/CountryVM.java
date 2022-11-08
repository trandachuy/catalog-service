package com.mediastep.beecow.catalog.web.rest.vm;

import com.mediastep.beecow.common.dto.CountryDTO;
import lombok.Data;

/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 30/10/2021
 * Author: Quy Luong <quy.luong@mediastep.com>
 *******************************************************************************/
@Data
public class CountryVM extends CountryDTO {
    private String flagEmoji;
}
