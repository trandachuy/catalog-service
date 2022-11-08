package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.dto.AddressDTO;
import com.mediastep.beecow.catalog.service.AddressService;
import com.mediastep.beecow.catalog.service.dto.FullShippingAddressDTO;
import com.mediastep.beecow.catalog.web.rest.vm.FullAddressVM;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class AddressResource {

    @Autowired
    private AddressService addressService;

    /*
    * Convert address string to code
    * ex: Lai Chau => VN-01
    * */
    @PostMapping("/address/convert")
    @Timed
    public ResponseEntity<Set<AddressDTO>> convert(@RequestBody AddressDTO address) {
        return ResponseEntity.ok().body(addressService.convertString2Code(address));
    }

    @GetMapping("/address/get-full-shipping-address")
    @Timed
    public FullShippingAddressDTO getFullShippingAddress(@ApiParam FullAddressVM request) {
        return addressService.getFullShippingAddress(request);
    }

}

