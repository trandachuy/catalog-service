package com.mediastep.beecow.catalog.domain;

import com.mediastep.beecow.catalog.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaypalCarrierTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaypalCarrier.class);
        PaypalCarrier paypalCarrier1 = new PaypalCarrier();
        paypalCarrier1.setId(1L);
        PaypalCarrier paypalCarrier2 = new PaypalCarrier();
        paypalCarrier2.setId(paypalCarrier1.getId());
        assertThat(paypalCarrier1).isEqualTo(paypalCarrier2);
        paypalCarrier2.setId(2L);
        assertThat(paypalCarrier1).isNotEqualTo(paypalCarrier2);
        paypalCarrier1.setId(null);
        assertThat(paypalCarrier1).isNotEqualTo(paypalCarrier2);
    }

}
