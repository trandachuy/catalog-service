package com.mediastep.beecow.catalog.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class WardValidationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String code;
    private String inCountry;
    private String outCountry;
    private Long districtId;
    private String districtCode;

    @Builder
    public WardValidationDTO(Long id, String code, String inCountry, String outCountry, Long districtId, String districtCode) {
        this.id = id;
        this.code = code;
        this.inCountry = inCountry;
        this.outCountry = outCountry;
        this.districtId = districtId;
        this.districtCode = districtCode;
    }
}
