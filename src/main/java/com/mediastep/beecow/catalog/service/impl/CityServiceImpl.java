package com.mediastep.beecow.catalog.service.impl;

import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.domain.City;
import com.mediastep.beecow.catalog.domain.City_;
import com.mediastep.beecow.catalog.dto.CityDistrictWardResponse;
import com.mediastep.beecow.catalog.dto.CitySimpleDTO;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.dto.WardNewDTO;
import com.mediastep.beecow.catalog.repository.CityRepository;
import com.mediastep.beecow.catalog.repository.specification.AddressSpecs;
import com.mediastep.beecow.catalog.repository.specification.CitySpecs;
import com.mediastep.beecow.catalog.service.CityService;
import com.mediastep.beecow.catalog.service.DistrictService;
import com.mediastep.beecow.catalog.service.WardService;
import com.mediastep.beecow.catalog.service.mapper.CityMapper;
import com.mediastep.beecow.common.dto.CityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing City.
 */
@SuppressWarnings({"unchecked"})
@Service
@Transactional
public class CityServiceImpl implements CityService{

    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    @Inject
    private CityRepository cityRepository;

    @Inject
    private CityMapper cityMapper;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CityService cityService;

    @Autowired
    private WardService wardService;
    @Autowired
    private AddressSpecs<City> addressSpecs;

    /**
     * Save a city.
     *
     * @param cityDTO the entity to save
     * @return the persisted entity
     */
    public CityDTO save(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);
        City city = cityMapper.cityDTOToCity(cityDTO);
        city = cityRepository.save(city);
        CityDTO result = cityMapper.cityToCityDTO(city);
        return result;
    }

    /**
     *  Get all the cities.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_ALL)
    public List<CityDTO> findAll() {
        log.debug("Request to get all Cities");
        List<CityDTO> result = cityRepository.findAll().stream()
            .map(cityMapper::cityToCityDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one city by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_BY_ID)
    public CityDTO findOne(Long id) {
        log.debug("Request to get City : {}", id);
        City city = cityRepository.findById(id).orElse(null);
        CityDTO cityDTO = cityMapper.cityToCityDTO(city);
        return cityDTO;
    }

    /**
     *  Get the "code" city.
     *
     *  @param code the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CityDTO findOne(String code) {
        log.debug("Request to get City : {}", code);
        City city = cityRepository.findOneByCode(code);
        CityDTO cityDTO = cityMapper.cityToCityDTO(city);
        return cityDTO;}

    /**
     *  Delete the  city by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete City : {}", id);
        cityRepository.deleteById(id);
    }

    /**
     *  Get all the cities by country code.
     *
     *  @param id
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CitySimpleDTO> findAll(Long id) {
        log.debug("Request to get all Cities");
        List<City> result = cityRepository.findAllByCountryId(id);
        return result.stream().map(city -> cityMapper.cityToCitySimpleDTO(city)).collect(Collectors.toList());
    }

    /**
     *  Get all the cities by keyword.
     *
     *  @param code
     *  @param keyword
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CitySimpleDTO> searchCity(String code, String keyword) {
        log.debug("Search city by keyword");
        List<City> cities = new ArrayList<>();
        if(!ObjectUtils.isEmpty(keyword)) {
             cities = cityRepository.findAll(CitySpecs.searchCity(code, keyword));
        }

        return cities.stream().map(city -> cityMapper.cityToCitySimpleDTO(city)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_BY_COUNTRY_CODE_AND_CODE)
    public List<CityDTO> searchCityByCountryAndCode(Set<Long> countryIds, String code) {
        log.debug("Search city by keyword");
        List<City> cities = new ArrayList<>();
        if(!ObjectUtils.isEmpty(code)) {
            cities = cityRepository.findAll(addressSpecs.searchAllByParentIdsAndCode(countryIds, "country", code));
        }
        return cityMapper.citiesToCityDTOs(cities);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_BY_COUNTRY_CODE_AND_NAME_OR_NAME)
    public List<CityDTO> searchCityByCountryAndCodeOrName(Set<Long> countryIds, String keyword) {
        List<City> cities = new ArrayList<>();
        if(!ObjectUtils.isEmpty(keyword)) {
            cities = cityRepository.findAll(addressSpecs.searchAllByParentIdsAndCodeOrName(countryIds, City_.COUNTRY, keyword));
        }
        return cityMapper.citiesToCityDTOs(cities);
    }

    /**
     *  Get all the cities by code.
     *
     *  @param code
     *  @return an entity
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_BY_CODE)
    public CitySimpleDTO searchCityByCode(String code) {
        log.debug("Request to get City : {}", code);

        return this.cityMapper.cityToCitySimpleDTO(this.cityRepository.findOneByCode(code));
    }

    @Override
    public List<CitySimpleDTO> findSimpleAllByCodes(List<String> codes) {
        log.debug("Request to find all Cities by codes : {}", codes);

        if (CollectionUtils.isEmpty(codes)) {
            return Collections.EMPTY_LIST;
        }

        return this.cityRepository.findAllByCodeIn(codes).parallelStream().map(cityMapper::cityToCitySimpleDTO).collect(Collectors.toList());
    }

    @Override
    public List<CityDTO> findAllByCodes(List<String> codes) {
        log.debug("Request to find all Cities by codes : {}", codes);

        if (CollectionUtils.isEmpty(codes)) {
            return Collections.EMPTY_LIST;
        }

        return this.cityRepository.findAllByCodeIn(codes).parallelStream().map(cityMapper::cityToCityDTO).collect(Collectors.toList());
    }

    @Override
    public CityDistrictWardResponse getCityDistrictWardByCode(String districtCode, String wardCode) {
        DistrictDTO districtDTO = new DistrictDTO();
        Optional<DistrictDTO> districtDTOOpt = this.districtService.findOne(districtCode);
        if (districtDTOOpt.isPresent()) {
            districtDTO = districtDTOOpt.get();
        }
        CityDTO cityDTO = new CityDTO();
        if (districtDTOOpt.isPresent()) {
            cityDTO = cityService.findOne(districtDTOOpt.get().getCityId());
        }
        WardNewDTO wardNewDTO = new WardNewDTO();
        if (wardCode != null) {
            Optional<WardNewDTO> tempWardNewDTO = wardService.findAllByDistrict(districtCode).stream()
                .filter(e -> Objects.equals(e.getCode(), wardCode)).findFirst();
            if (tempWardNewDTO.isPresent()) {
                wardNewDTO = tempWardNewDTO.get();
            }
        }
        return CityDistrictWardResponse.builder()
            .cityDTO(cityDTO)
            .districtDTO(districtDTO)
            .wardNewDTO(wardNewDTO)
            .build();
    }

    @Override
    public List<CityDTO> findAllByIdIn(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return new ArrayList<>();
        return this.cityRepository.findAllByIdIn(ids).parallelStream().map(cityMapper::cityToCityDTO).collect(Collectors.toList());

    }
}
