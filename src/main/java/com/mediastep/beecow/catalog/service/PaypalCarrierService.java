package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.service.dto.PaypalCarrierDTO;
import com.mediastep.beecow.catalog.service.dto.PaypalCarrierResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mediastep.beecow.catalog.domain.PaypalCarrier}.
 */
public interface PaypalCarrierService {

    /**
     * Save a paypalCarrier.
     *
     * @param paypalCarrierDTO the entity to save.
     * @return the persisted entity.
     */
    PaypalCarrierDTO save(PaypalCarrierDTO paypalCarrierDTO);

    /**
     * Get all the paypalCarriers.
     *
     * @return the list of entities.
     */
    List<PaypalCarrierDTO> findAll();


    /**
     * Get the "id" paypalCarrier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaypalCarrierDTO> findOne(Long id);

    /**
     * Delete the "id" paypalCarrier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<PaypalCarrierResponse> searchPaypalCarrier();
}
