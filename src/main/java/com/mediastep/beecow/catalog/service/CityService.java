package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.dto.CityDistrictWardResponse;
import com.mediastep.beecow.catalog.dto.CitySimpleDTO;
import com.mediastep.beecow.common.dto.CityDTO;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing City.
 */
public interface CityService {

    /**
     * Save a city.
     *
     * @param cityDTO the entity to save
     * @return the persisted entity
     */
    CityDTO save(CityDTO cityDTO);

    /**
     *  Get all the cities.
     *
     *  @return the list of entities
     */
    List<CityDTO> findAll();

    /**
     *  Get the "id" city.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CityDTO findOne(Long id);

    /**
     *  Get the "code" city.
     *
     *  @param code the id of the entity
     *  @return the entity
     */
    CityDTO findOne(String code);

    /**
     *  Delete the "id" city.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get all the cities by country code.
     *
     *  @param id
     *  @return the list of entities
     */
    List<CitySimpleDTO> findAll(Long id);

    /**
     *  Get all the cities by keyword.
     *
     *  @param code
     *  @param keyword
     *  @return the list of entities
     */
    List<CitySimpleDTO> searchCity(String code, String keyword);

    List<CityDTO> searchCityByCountryAndCode(Set<Long> countryIds, String code);

    List<CityDTO> searchCityByCountryAndCodeOrName(Set<Long> countryIds, String keyword);

    /**
     *  Get all the cities by code.
     *
     *  @param code
     *  @return an entity
     */
    CitySimpleDTO searchCityByCode(String code);

    List<CitySimpleDTO> findSimpleAllByCodes(List<String> codes);

    List<CityDTO> findAllByCodes(List<String> codes);

    CityDistrictWardResponse getCityDistrictWardByCode(String districtCode, String wardCode);

    List<CityDTO> findAllByIdIn(Set<Long> ids);
}
