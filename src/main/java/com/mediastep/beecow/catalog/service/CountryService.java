package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.web.rest.vm.CountryVM;
import com.mediastep.beecow.common.dto.CountryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Country.
 */
public interface CountryService {

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save
     * @return the persisted entity
     */
    CountryDTO save(CountryDTO countryDTO);

    /**
     *  Get all the countries.
     *
     *  @param pageable the pagination information
     *  @param withCities
     * @return the list of entities
     */
    Page<CountryVM> findAll(Pageable pageable, Boolean withCities);

    /**
     *  Get the "id" country.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CountryVM findOne(Long id);

    /**
     *  Get the "code" country.
     *
     *  @param code the code of the entity
     *  @return the entity
     */
    CountryVM findOneByCode(String code, Boolean withCities);

    List<CountryVM> findAllByCodes(List<String> codes, Boolean withCities);

    /**
     *  Delete the "id" country.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);


	List<CountryVM> getActiveCountries();

    List<CountryVM> getActiveCountriesByCode(String code);

    List<CountryVM> getCountriesCitiesByCode(String code);

    String getAddressByCode(String cityCode, String districtCode, String wardCode);

    String findCurrencySymbolByCurrencyCode(String currencyCode);
}
