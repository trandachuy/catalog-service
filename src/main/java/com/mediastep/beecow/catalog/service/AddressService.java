package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.dto.AddressDTO;
import com.mediastep.beecow.catalog.service.dto.FullShippingAddressDTO;
import com.mediastep.beecow.catalog.web.rest.vm.FullAddressVM;
import com.paypal.orders.AddressPortable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface AddressService {
    Set<AddressDTO> convertString2Code(AddressDTO address);

    FullShippingAddressDTO getFullShippingAddress(FullAddressVM request);
}
