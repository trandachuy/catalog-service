package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing District.
 */
public interface DistrictService {

    /**
     * Save a district.
     *
     * @param districtDTO the entity to save
     * @return the persisted entity
     */
    DistrictDTO save(DistrictDTO districtDTO);

    /**
     * Get all the districts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DistrictDTO> findAll(Pageable pageable);

    /**
     * Get all the districts by city ID.
     *
     * @param cityId the city id
     * @return the list of entities
     */
    List<DistrictDTO> findAll(Long cityId);

    List<DistrictValidationDTO> getAllDistrictOfCountry(String countryCode);

    /**
     * Find all list.
     *
     * @param cityCode the city code
     * @return the list
     */
    List<DistrictDTO> findAll(String cityCode);

    /**
     * Get the "id" district.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DistrictDTO> findOne(Long id);

    /**
     * Find one optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<DistrictDTO> findOne(String code);

    /**
     * Delete the "id" district.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<DistrictDTO> findAllByCodes(List<String> codes);

    List<DistrictDTO> findAllByCityIdsAndName(Set<Long> cityIds, String name);

    List<DistrictDTO> searchDistrictsByCityAndCodeOrName(Set<Long> countryIds, String keyword);

    List<DistrictDTO> searchAllByCodeOrName(String keyword);

    List<DistrictDTO> findAllByIdIn(Set<Long> ids);

}
