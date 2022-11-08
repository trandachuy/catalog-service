package com.mediastep.beecow.catalog.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaypalCarrierMapperTest {

    private PaypalCarrierMapper paypalCarrierMapper;

    @BeforeEach
    public void setUp() {
        paypalCarrierMapper = new PaypalCarrierMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paypalCarrierMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paypalCarrierMapper.fromId(null)).isNull();
    }
}
