package com.mediastep.beecow.catalog.service.dto;

import com.mediastep.beecow.catalog.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaypalCarrierDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaypalCarrierDTO.class);
        PaypalCarrierDTO paypalCarrierDTO1 = new PaypalCarrierDTO();
        paypalCarrierDTO1.setId(1L);
        PaypalCarrierDTO paypalCarrierDTO2 = new PaypalCarrierDTO();
        assertThat(paypalCarrierDTO1).isNotEqualTo(paypalCarrierDTO2);
        paypalCarrierDTO2.setId(paypalCarrierDTO1.getId());
        assertThat(paypalCarrierDTO1).isEqualTo(paypalCarrierDTO2);
        paypalCarrierDTO2.setId(2L);
        assertThat(paypalCarrierDTO1).isNotEqualTo(paypalCarrierDTO2);
        paypalCarrierDTO1.setId(null);
        assertThat(paypalCarrierDTO1).isNotEqualTo(paypalCarrierDTO2);
    }
}
