/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 10/11/2018
 * Author: Dai Mai <email: dai.mai@mediastep.com>
 */

package com.mediastep.beecow.catalog.web.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.dto.view.DistrictView;
import com.mediastep.beecow.catalog.service.CityService;
import com.mediastep.beecow.catalog.service.DistrictService;
import com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO;
import com.mediastep.beecow.catalog.web.rest.errors.BadRequestAlertException;
import com.mediastep.beecow.common.aop.annotation.PublishedOpenApi;
import com.mediastep.beecow.common.dto.CityDTO;
import com.mediastep.beecow.common.dto.WardDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing District.
 */
@RestController
@RequestMapping("/api")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    private static final String ENTITY_NAME = "district";

    private final DistrictService districtService;

    private final CityService cityService;

    public DistrictResource(DistrictService districtService, CityService cityService) {
        this.districtService = districtService;
        this.cityService = cityService;
    }

    /**
     * POST  /districts : Create a new district.
     *
     * @param districtDTO the districtDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new districtDTO, or with status 400 (Bad Request) if the district has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/districts")
    @Timed
    public ResponseEntity<DistrictDTO> createDistrict(@RequestBody DistrictDTO districtDTO) throws URISyntaxException {
        log.debug("REST request to save District : {}", districtDTO);
        if (districtDTO.getId() != null) {
            throw new BadRequestAlertException("A new district cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DistrictDTO result = districtService.save(districtDTO);
        return ResponseEntity.created(new URI("/api/districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /districts : Updates an existing district.
     *
     * @param districtDTO the districtDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated districtDTO,
     * or with status 400 (Bad Request) if the districtDTO is not valid,
     * or with status 500 (Internal Server Error) if the districtDTO couldn't be updated
     */
    @PutMapping("/districts")
    @Timed
    public ResponseEntity<DistrictDTO> updateDistrict(@RequestBody DistrictDTO districtDTO) {
        log.debug("REST request to update District : {}", districtDTO);
        if (districtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DistrictDTO result = districtService.save(districtDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, districtDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /districts : get all the districts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of districts in body
     */
    @GetMapping("/districts")
    @Timed
    public ResponseEntity<List<DistrictDTO>> getAllDistricts(Pageable pageable) {
        log.debug("REST request to get a page of Districts");
        Page<DistrictDTO> page = districtService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /districts/validation : get all the districts for validation.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of districts in body
     */
    @GetMapping("/districts/validation")
    @Timed
    public ResponseEntity<List<DistrictValidationDTO>> getAllDistrictOfCountry(@RequestParam(value = "countryCode", required = false) String countryCode) {
        log.debug("REST request to get a page of Districts");
        List<DistrictValidationDTO> page = districtService.getAllDistrictOfCountry(StringUtils.defaultIfEmpty(countryCode, Constants.COUNTRY_CODE_VN));
        return new ResponseEntity<>(page, null, HttpStatus.OK);
    }

    /**
     * GET  /city/:code/districts : get all districts in simple type by "code" city code.
     *
     * @param code the code
     * @return the ResponseEntity with status 200 (OK) and with body the districtDTO, or with status 404 (Not Found)
     */
    @ApiOperation("get districts by city code")
    @GetMapping("/city/{code}/districts")
    @Timed
    @JsonView(DistrictView.Simple.class)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_DISTRICT_BY_CITY_CODE)
    @PublishedOpenApi
    public List<DistrictDTO> getAllSimpleDistricts(@ApiParam("City's code") @PathVariable("code") String code) {
        log.debug("REST request to get a page of Districts");
        List<DistrictDTO> list = new ArrayList<>();
        CityDTO cityDTO = this.cityService.findOne(code);
        if (cityDTO != null) {
            list = this.districtService.findAll(cityDTO.getId());
        }
        return list;
    }

    @ApiOperation("search district by city code and name")
    @GetMapping("/city/{cityId}/districts/{name}")
    @Timed
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_DISTRICT_BY_CITY_CODE_AND_NAME)
    public List<DistrictDTO> getDistrictByCityCodeAndName(@ApiParam("city id") @PathVariable Long cityId, @ApiParam("district name") @PathVariable String name) {
        return districtService.findAllByCityIdsAndName(new HashSet<>(Collections.singletonList(cityId)), name);
    }

    /**
     * GET  /districts/:id : get the "id" district.
     *
     * @param id the id of the districtDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the districtDTO, or with status 404 (Not Found)
     */
    @GetMapping("/districts/{id}")
    @Timed
    public ResponseEntity<DistrictDTO> getDistrict(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        Optional<DistrictDTO> districtDTO = districtService.findOne(id);
        return districtDTO.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /districts/:code : get the "code" districts.
     *
     * @param code the code of the DistrictDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the DistrictDTO, or with status 404 (Not Found)
     */
    @GetMapping("/districts/code/{code}")
    @Timed
    @JsonView(DistrictView.Detail.class)
    public ResponseEntity<DistrictDTO> getDistrictsByCode(@PathVariable String code) {
        log.debug("REST request to get District : {}", code);
        Optional<DistrictDTO> districtDTO = this.districtService.findOne(code);
        return districtDTO
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /districts/:id : delete the "id" district.
     *
     * @param id the id of the districtDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/districts/{id}")
    @Timed
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        districtService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, id.toString())).build();
    }


    /*************************************************************
     * OLD IMPLEMENT OF GET DISTRICT, WILL DEPRECATED IN FUTURE  *
     *************************************************************/


    /**
     * POST  /wards : Create a new ward.
     *
     * @param wardDTO the wardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wardDTO, or with status 400 (Bad Request) if the ward has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wards")
    @Timed
    @Deprecated
    @JsonView(DistrictView.Detail.class)
    public ResponseEntity<?> createWard(@RequestBody WardDTO wardDTO) throws URISyntaxException {
        DistrictDTO districtDTO = new DistrictDTO(wardDTO.getId(), wardDTO.getCode(), wardDTO.getInCountry(), wardDTO.getOutCountry(), wardDTO.getZone(), wardDTO.getCityId());
        return this.createDistrict(districtDTO);
    }

    /**
     * PUT  /wards : Updates an existing ward.
     *
     * @param wardDTO the wardDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wardDTO,
     * or with status 400 (Bad Request) if the wardDTO is not valid,
     * or with status 500 (Internal Server Error) if the wardDTO couldnt be updated
     */
    @PutMapping("/wards")
    @Timed
    @Deprecated
    @JsonView(DistrictView.Detail.class)
    public ResponseEntity<?> updateWard(@RequestBody WardDTO wardDTO) {
        DistrictDTO districtDTO = new DistrictDTO(wardDTO.getId(), wardDTO.getCode(), wardDTO.getInCountry(), wardDTO.getOutCountry(), wardDTO.getZone(), wardDTO.getCityId());
        return this.updateDistrict(districtDTO);
    }

    /**
     * GET  /wards : get all the wards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wards in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/wards")
    @Timed
    @Deprecated
    @JsonView(DistrictView.Detail.class)
    public ResponseEntity<?> getAllWards(@ApiParam Pageable pageable)
        throws URISyntaxException {
        return this.getAllDistricts(pageable);
    }

    /**
     * GET  /wards : get all the wards.
     *
     * @param code the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wards in body
     */
    @ApiOperation("get wards by city code")
    @GetMapping("/city/{code}/wards")
    @Timed
    @Deprecated
    @JsonView(DistrictView.Simple.class)
    public List<?> getAllSimpleWards(@ApiParam("city's code") @PathVariable("code") String code) {
        return this.getAllSimpleDistricts(code);
    }

    /**
     * GET  /wards/:id : get the "id" ward.
     *
     * @param id the id of the wardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wards/{id}")
    @Timed
    @Deprecated
    @JsonView(DistrictView.Detail.class)
    public ResponseEntity<?> getWard(@PathVariable Long id) {
        return this.getDistrict(id);
    }

    /**
     * GET  /wards/:code : get the "code" ward.
     *
     * @param code the code of the wardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ward/{code}")
    @Timed
    @Deprecated
    @JsonView(DistrictView.Detail.class)
    public ResponseEntity<?> getWardByCode(@PathVariable String code) {
        return this.getDistrictsByCode(code);
    }

    /**
     * DELETE  /wards/:id : delete the "id" ward.
     *
     * @param id the id of the wardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wards/{id}")
    @Timed
    @Deprecated
    public ResponseEntity<Void> deleteWard(@PathVariable Long id) {
        return this.deleteDistrict(id);
    }

}
