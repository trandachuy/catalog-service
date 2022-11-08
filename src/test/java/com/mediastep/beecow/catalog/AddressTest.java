package com.mediastep.beecow.catalog;

import com.mediastep.beecow.catalog.dto.AddressDTO;
import com.mediastep.beecow.catalog.service.AddressService;
import com.paypal.orders.AddressPortable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogServicesApp.class)
public class AddressTest {

    @Autowired
    private AddressService addressService;

    @Test
    @Transactional
    public void convertString2Code() {
        AddressDTO address = new AddressDTO();
        address.setAddressLine1("19 Ho Van Hue");
        address.setAdminArea1("Ho Chi Minh");
        address.setAdminArea2("District 1");
        address.setCountryCode("VN");
        Set<AddressDTO> set = addressService.convertString2Code(address);
        assertTrue(set.size() > 0);
    }
}
