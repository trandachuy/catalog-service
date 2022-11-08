package com.mediastep.beecow.catalog.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class DistrictValidationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String code;
    private String inCountry;
    private String outCountry;
    private String zone;
    private Long cityId;
    private String cityCode;

    @Builder
    public DistrictValidationDTO(Long id, String code, String inCountry, String outCountry, String zone, Long cityId, String cityCode) {
        this.id = id;
        this.code = code;
        this.inCountry = inCountry;
        this.outCountry = outCountry;
        this.zone = zone;
        this.cityId = cityId;
        this.cityCode = cityCode;
    }
}
