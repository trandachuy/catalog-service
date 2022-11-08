/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 10/11/2018
 * Author: Dai Mai <email: dai.mai@mediastep.com>
 */

package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.dto.WardNewDTO;
import com.mediastep.beecow.catalog.service.WardService;
import com.mediastep.beecow.catalog.service.dto.WardValidationDTO;
import com.mediastep.beecow.catalog.web.rest.errors.BadRequestAlertException;
import com.mediastep.beecow.common.aop.annotation.PublishedOpenApi;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ward.
 */
@RestController
@RequestMapping("/api")
public class WardResource {

    private final Logger log = LoggerFactory.getLogger(WardResource.class);

    private static final String ENTITY_NAME = "ward";

    private final WardService wardService;

    public WardResource(WardService wardService) {
        this.wardService = wardService;
    }

    /**
     * POST  /wards/new : Create a new ward.
     *
     * @param wardDTO the wardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wardDTO, or with status 400 (Bad Request) if the ward has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wards/new")
    @Timed
    public ResponseEntity<WardNewDTO> createWard(@RequestBody WardNewDTO wardDTO) throws URISyntaxException {
        log.info("REST request to save Ward : {}", wardDTO);
        if (wardDTO.getId() != null) {
            throw new BadRequestAlertException("A new ward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WardNewDTO result = wardService.save(wardDTO);
        return ResponseEntity.created(new URI("/api/wards/new/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wards/new : Updates an existing ward.
     *
     * @param wardDTO the wardDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wardDTO,
     * or with status 400 (Bad Request) if the wardDTO is not valid,
     * or with status 500 (Internal Server Error) if the wardDTO couldn't be updated
     */
    @PutMapping("/wards/new")
    @Timed
    public ResponseEntity<WardNewDTO> updateWard(@RequestBody WardNewDTO wardDTO) {
        log.info("REST request to update Ward : {}", wardDTO);
        if (wardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WardNewDTO result = wardService.save(wardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, wardDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wards/new : get all the wards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wards in body
     */
    @GetMapping("/wards/new")
    @Timed
    public ResponseEntity<List<WardNewDTO>> getAllWards(Pageable pageable) {
        log.info("REST request to get a page of Wards");
        Page<WardNewDTO> page = wardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wards/validation : get all the wards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wards in body
     */
    @GetMapping("/wards/validation")
    @Timed
    public ResponseEntity<List<WardValidationDTO>> getAllWardOfCountry(@RequestParam(value = "countryCode", required = false) String countryCode) {
        log.info("REST request to get a page of Wards");
        List<WardValidationDTO> page = wardService.getAllWardOfCountry(StringUtils.defaultIfEmpty(countryCode, Constants.COUNTRY_CODE_VN));
        return new ResponseEntity<>(page, null, HttpStatus.OK);
    }

    /**
     * GET  /wards/new/:id : get the "id" ward.
     *
     * @param id the id of the wardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wards/new/{id}")
    @Timed
    public ResponseEntity<WardNewDTO> getWard(@PathVariable Long id) {
        log.info("REST request to get Ward : {}", id);
        Optional<WardNewDTO> wardDTO = wardService.findOne(id);
        return wardDTO.map(result -> new ResponseEntity<>(result, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /wards/code/:code : get the "code" ward.
     *
     * @param code the code of the wardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wards/code/{code}")
    @Timed
    public ResponseEntity<WardNewDTO> getWard(@PathVariable String code) {
        log.info("REST request to get Ward by code : {}", code);
        Optional<WardNewDTO> wardDTO = wardService.findOneByCode(code);
        return wardDTO.map(result -> new ResponseEntity<>(result, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /district/:code/wards : get list ward by district code.
     *
     * @param code the district code of the wardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wardDTO, or with status 404 (Not Found)
     */
    @ApiOperation("get wards by district code")
    @GetMapping("/district/{code}/wards")
    @Timed
    @PublishedOpenApi
    public ResponseEntity<List<WardNewDTO>> getWardByDistrictCode(@ApiParam("District's code") @PathVariable String code) {
        log.info("REST request to get Ward by district code : {}", code);
        List<WardNewDTO> wardDTOs = wardService.findAllByDistrict(code);
        return new ResponseEntity<>(wardDTOs, HttpStatus.OK);
    }

    @ApiOperation("search ward by district and name")
    @GetMapping("/district/{districtId}/ward/{name}")
    @Timed
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_WARD_BY_DISTRICT_CODE_AND_NAME)
    public List<WardNewDTO> getDistrictByCityCodeAndName(@ApiParam("district id") @PathVariable Long districtId, @ApiParam("district name") @PathVariable String name) {
        return wardService.findAllByDistrictIdsAndName(new HashSet<>(Collections.singletonList(districtId)), name);
    }

    /**
     * DELETE  /wards/new/:id : delete the "id" ward.
     *
     * @param id the id of the wardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wards/new/{id}")
    @Timed
    public ResponseEntity<Void> deleteWard(@PathVariable Long id) {
        log.info("REST request to delete Ward : {}", id);
        wardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, id.toString())).build();
    }

}
