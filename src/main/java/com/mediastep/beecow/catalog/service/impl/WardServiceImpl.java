package com.mediastep.beecow.catalog.service.impl;

import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.domain.District;
import com.mediastep.beecow.catalog.domain.Ward;
import com.mediastep.beecow.catalog.domain.Ward_;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.dto.WardNewDTO;
import com.mediastep.beecow.catalog.repository.WardNativeRepository;
import com.mediastep.beecow.catalog.repository.WardRepository;
import com.mediastep.beecow.catalog.repository.specification.AddressSpecs;
import com.mediastep.beecow.catalog.service.WardService;
import com.mediastep.beecow.catalog.service.dto.WardValidationDTO;
import com.mediastep.beecow.catalog.service.mapper.WardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Ward.
 */
@Service
@Transactional
public class WardServiceImpl implements WardService {

    private final Logger log = LoggerFactory.getLogger(WardServiceImpl.class);

    private final WardRepository wardRepository;

    private final WardMapper wardMapper;

    private final WardNativeRepository wardNativeRepository;

    /**
     * Instantiates a new Ward service.
     *
     * @param wardRepository the ward repository
     * @param wardMapper     the ward mapper
     */
    public WardServiceImpl(WardRepository wardRepository, WardMapper wardMapper,
                           WardNativeRepository wardNativeRepository) {
        this.wardRepository = wardRepository;
        this.wardMapper = wardMapper;
        this.wardNativeRepository = wardNativeRepository;
    }

    @Autowired
    private AddressSpecs<Ward> addressSpecs;

    /**
     * Save a ward.
     *
     * @param wardDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WardNewDTO save(WardNewDTO wardDTO) {
        log.debug("Request to save Ward : {}", wardDTO);
        Ward ward = wardMapper.toEntity(wardDTO);
        ward = wardRepository.save(ward);
        WardNewDTO result = wardMapper.toDto(ward);
        return result;
    }

    /**
     * Get all the wards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_WARD_ALL)
    public Page<WardNewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Wards");
        return wardRepository.findAll(pageable)
            .map(wardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_WARD_BY_COUNTRY, key = "#p0", unless = "#p0 == null or #result.empty")
    public List<WardValidationDTO> getAllWardOfCountry(String countryCode) {
        log.debug("Request to get all Wards of country code {}", countryCode);
        return wardNativeRepository.getAllWardOfCountry(countryCode);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_WARD_BY_DISTRICT_CODE)
    public List<WardNewDTO> findAllByDistrict(String districtCode) {
        log.debug("Request to get all Wards by district code {}", districtCode);
        return this.wardMapper.toDto(this.wardRepository.findAllByDistrict_CodeOrderByInCountryAsc(districtCode));
    }

    /**
     * Get one ward by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WardNewDTO> findOne(Long id) {
        log.debug("Request to get Ward : {}", id);
        return wardRepository.findById(id)
            .map(wardMapper::toDto);
    }

    /**
     * Find one by code optional.
     *
     * @param wardCode the ward code
     * @return the optional
     */
    @Override
    public Optional<WardNewDTO> findOneByCode(String wardCode) {
        log.debug("Request to get Ward by code: {}", wardCode);
        return wardRepository.findByCode(wardCode).map(wardMapper::toDto);
    }

    /**
     * Delete the ward by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ward : {}", id);
        wardRepository.deleteById(id);
    }

    @Override
    public List<WardNewDTO> findAllByCodes(List<String> codes) {
        log.debug("Request to find all Wards by codes : {}", codes);

        if (CollectionUtils.isEmpty(codes)) {
            return Collections.EMPTY_LIST;
        }

        return wardRepository.findByCodeIn(codes).parallelStream().map(wardMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<WardNewDTO> findAllByDistrictIdsAndName(Set<Long> districtIds, String name) {
        if (CollectionUtils.isEmpty(districtIds) || StringUtils.isEmpty(name)) {
            return new ArrayList<>();
        }
        List<Ward> wards = wardRepository.findAll(addressSpecs.searchAllByParentIdsAndName(districtIds, "district", name));
        return wardMapper.toDto(wards);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_WARD_BY_DISTRICT_AND_CODE_OR_NAME)
    public List<WardNewDTO> searchWardByDistrictAndCodeOrName(Set<Long> districtIds, String keyword) {
        if (CollectionUtils.isEmpty(districtIds) || StringUtils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        List<Ward> wards = wardRepository.findAll(addressSpecs.searchAllByParentIdsAndCodeOrName(districtIds, Ward_.DISTRICT, keyword));
        return wardMapper.toDto(wards);
    }

    @Override
    public List<WardNewDTO> findByIdIn(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return new ArrayList<>();
        return wardRepository.findAllById(ids).parallelStream().map(wardMapper::toDto).collect(Collectors.toList());
    }
}
