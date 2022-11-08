package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.service.dto.ShipperDTO;
import com.mediastep.beecow.catalog.service.dto.SimpleShipperDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Shipper.
 */
public interface ShipperService {

    /**
     * Save a shipper.
     *
     * @param shipperDTO the entity to save
     * @return the persisted entity
     */
    ShipperDTO save(ShipperDTO shipperDTO);

    /**
     *  Get all the shippers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ShipperDTO> findAll(Pageable pageable);

    /**
     *  Get all the shippers.
     *
     *  @param countryId the pagination information
     *  @return the list of entities
     */
    List<SimpleShipperDTO> findAll(Long countryId);

    /**
     *  Get the "id" shipper.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ShipperDTO findOne(Long id);

    /**
     *  Delete the "id" shipper.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
