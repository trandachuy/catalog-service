package com.mediastep.beecow.catalog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaypalCarrierResponse {
    String countryName;
    List<PaypalCarrierDTO> paypalCarrierList;
}
