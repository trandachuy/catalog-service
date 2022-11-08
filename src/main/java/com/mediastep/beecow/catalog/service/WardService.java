package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.domain.Ward;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.dto.WardNewDTO;
import com.mediastep.beecow.catalog.service.dto.WardValidationDTO;
import com.mediastep.beecow.common.dto.CityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing Ward.
 */
public interface WardService {

    /**
     * Save a ward.
     *
     * @param wardDTO the entity to save
     * @return the persisted entity
     */
    WardNewDTO save(WardNewDTO wardDTO);

    /**
     * Get all the wards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WardNewDTO> findAll(Pageable pageable);

    List<WardValidationDTO> getAllWardOfCountry(String countryCode);

    /**
     * Find all by district list.
     *
     * @param districtCode the district code
     * @return the list
     */
    List<WardNewDTO> findAllByDistrict(String districtCode);

    /**
     * Get the "id" ward.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WardNewDTO> findOne(Long id);

    /**
     * Find one by code optional.
     *
     * @param wardCode the ward code
     * @return the optional
     */
    Optional<WardNewDTO> findOneByCode(String wardCode);

    /**
     * Delete the "id" ward.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	List<WardNewDTO> findAllByCodes(List<String> codes);

    List<WardNewDTO> findAllByDistrictIdsAndName(Set<Long> districtIds, String name);

    List<WardNewDTO> searchWardByDistrictAndCodeOrName(Set<Long> districtIds, String keyword);

    List<WardNewDTO> findByIdIn(Set<Long> ids);

}
