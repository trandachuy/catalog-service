package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.service.PaypalCarrierService;
import com.mediastep.beecow.catalog.service.dto.PaypalCarrierDTO;
import com.mediastep.beecow.catalog.service.dto.PaypalCarrierResponse;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mediastep.beecow.catalog.domain.PaypalCarrier}.
 */
@RestController
@RequestMapping("/api")
public class PaypalCarrierResource {

    private final Logger log = LoggerFactory.getLogger(PaypalCarrierResource.class);

    private static final String ENTITY_NAME = "catalogServicesPaypalCarrier";

    private final PaypalCarrierService paypalCarrierService;

    public PaypalCarrierResource(PaypalCarrierService paypalCarrierService) {
        this.paypalCarrierService = paypalCarrierService;
    }

    /**
     * {@code GET  /paypal-carriers/:id} : get the "id" paypalCarrier.
     *
     * @param id the id of the paypalCarrierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paypalCarrierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paypal-carriers/{id}")
    public ResponseEntity<PaypalCarrierDTO> getPaypalCarrier(@PathVariable Long id) {
        log.debug("REST request to get PaypalCarrier : {}", id);
        Optional<PaypalCarrierDTO> paypalCarrierDTO = paypalCarrierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paypalCarrierDTO);
    }

    /**
     * {@code DELETE  /paypal-carriers/:id} : delete the "id" paypalCarrier.
     *
     * @param id the id of the paypalCarrierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paypal-carriers/{id}")
    public ResponseEntity<Void> deletePaypalCarrier(@PathVariable Long id) {
        log.debug("REST request to delete PaypalCarrier : {}", id);
        paypalCarrierService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paypal-carriers/search")
    public List<PaypalCarrierResponse> searchPaypalCarrier() {
        return paypalCarrierService.searchPaypalCarrier();
    }
}
