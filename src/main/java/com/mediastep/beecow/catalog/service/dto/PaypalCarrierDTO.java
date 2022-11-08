package com.mediastep.beecow.catalog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.mediastep.beecow.catalog.domain.PaypalCarrier} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaypalCarrierDTO implements Serializable {

    private Long id;

    private String carrierName;

    private String carrierCode;

    private String regionCode;

    private String countryName;
}
