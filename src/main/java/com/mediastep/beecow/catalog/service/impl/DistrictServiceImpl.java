package com.mediastep.beecow.catalog.service.impl;

import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.domain.City;
import com.mediastep.beecow.catalog.domain.District;
import com.mediastep.beecow.catalog.domain.District_;
import com.mediastep.beecow.catalog.dto.CitySimpleDTO;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.repository.CityRepository;
import com.mediastep.beecow.catalog.repository.DistrictNativeRepository;
import com.mediastep.beecow.catalog.repository.DistrictRepository;
import com.mediastep.beecow.catalog.repository.specification.AddressSpecs;
import com.mediastep.beecow.catalog.service.CityService;
import com.mediastep.beecow.catalog.service.DistrictService;
import com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO;
import com.mediastep.beecow.catalog.service.mapper.DistrictMapper;
import com.mediastep.beecow.common.dto.CityDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing District.
 */
@Service
@Transactional
public class DistrictServiceImpl implements DistrictService {

    private final Logger log = LoggerFactory.getLogger(DistrictServiceImpl.class);

    private final DistrictRepository districtRepository;

    private final DistrictMapper districtMapper;

    private final DistrictNativeRepository districtNativeRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository, DistrictMapper districtMapper,
                               DistrictNativeRepository districtNativeRepository) {
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
        this.districtNativeRepository = districtNativeRepository;
    }

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private AddressSpecs<District> addressSpecs;

    /**
     * Save a district.
     *
     * @param districtDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DistrictDTO save(DistrictDTO districtDTO) {
        log.debug("Request to save District : {}", districtDTO);
        District district = districtMapper.toEntity(districtDTO);
        district = districtRepository.save(district);
        DistrictDTO result = districtMapper.toDto(district);
        return result;
    }

    /**
     * Get all the districts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_DISTRICT_ALL)
    public Page<DistrictDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Districts");
        return districtRepository.findAll(pageable)
            .map(districtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_DISTRICT_BY_COUNTRY, key = "#p0", unless = "#p0 == null or #result.empty")
    public List<DistrictValidationDTO> getAllDistrictOfCountry(String countryCode) {
        log.debug("Request to get all Districts");
        return districtNativeRepository.getAllDistrictOfCountry(countryCode);
    }

    /**
     * Get all the districts by city ID.
     *
     * @param cityId the city id
     * @return the list of entities
     */
    @Override
    public List<DistrictDTO> findAll(Long cityId) {
        log.debug("Request to get all Districts by city id {}", cityId);

        List<DistrictDTO> rs = this.districtRepository.findByCity_Id(cityId).stream().map(this.districtMapper::toDto)
            .collect(Collectors.toList());

        return rs;
    }

    @Override
    public List<DistrictDTO> findAll(String cityCode) {
        log.debug("Request get all Districts by city code {}", cityCode);

        List<DistrictDTO> rs = this.districtRepository.findByCity_Code(cityCode).stream().map(this.districtMapper::toDto)
            .collect(Collectors.toList());

        return rs;
    }

    /**
     * Get one district by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DistrictDTO> findOne(Long id) {
        log.debug("Request to get District by ID: {}", id);
        return districtRepository.findById(id)
            .map(districtMapper::toDto);
    }

    /**
     * Find one optional.
     *
     * @param code the code
     * @return the optional
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DistrictDTO> findOne(String code) {
        log.debug("Request to get District by code: {}", code);
        return this.districtRepository.findByCode(code).map(this.districtMapper::toDto);
    }

    /**
     * Delete the district by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete District : {}", id);
        districtRepository.deleteById(id);
    }

    @Override
    public List<DistrictDTO> findAllByCodes(List<String> codes) {
        log.debug("Request to find all Districts by codes : {}", codes);

        if (CollectionUtils.isEmpty(codes)) {
            return Collections.EMPTY_LIST;
        }

        return districtRepository.findByCodeIn(codes).parallelStream().map(districtMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DistrictDTO> findAllByCityIdsAndName(Set<Long> cityIds, String name) {
        if (CollectionUtils.isEmpty(cityIds) || StringUtils.isEmpty(name)) {
            return new ArrayList<>();
        }
        List<District> districts = districtRepository.findAll(addressSpecs.searchAllByParentIdsAndName(cityIds, "city", name));
        return districtMapper.toDto(districts);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_DISTRICT_BY_CITY_AND_CODE_OR_NAME)
    public List<DistrictDTO> searchDistrictsByCityAndCodeOrName(Set<Long> cityIds, String keyword) {
        if (CollectionUtils.isEmpty(cityIds) || StringUtils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        List<District> districts = districtRepository.findAll(addressSpecs.searchAllByParentIdsAndCodeOrName(cityIds, District_.CITY, keyword));
        return districtMapper.toDto(districts);
    }

    @Override
    public List<DistrictDTO> searchAllByCodeOrName(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        List<District> districts = districtRepository.findAll(addressSpecs.searchAllByCodeOrName(keyword));
        return districtMapper.toDto(districts);
    }

    @Override
    public List<DistrictDTO> findAllByIdIn(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return districtRepository.findAllById(ids).parallelStream().map(districtMapper::toDto).collect(Collectors.toList());
    }
}
