package com.mediastep.beecow.catalog.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A PaypalCarrier.
 */
@Entity
@Table(name = "paypal_carrier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaypalCarrier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "carrier_code")
    private String carrierCode;

    @Column(name = "region_code")
    private String regionCode;

    @Column(name = "country_name")
    private String countryName;
}
