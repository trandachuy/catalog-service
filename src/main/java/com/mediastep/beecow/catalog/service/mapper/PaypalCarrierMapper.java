package com.mediastep.beecow.catalog.service.mapper;


import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.catalog.service.dto.PaypalCarrierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaypalCarrier} and its DTO {@link PaypalCarrierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaypalCarrierMapper extends EntityMapper<PaypalCarrierDTO, PaypalCarrier> {



    default PaypalCarrier fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaypalCarrier paypalCarrier = new PaypalCarrier();
        paypalCarrier.setId(id);
        return paypalCarrier;
    }
}
