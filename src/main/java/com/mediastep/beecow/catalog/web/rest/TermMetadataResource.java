/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.service.TermMetadataService;
import com.mediastep.beecow.common.dto.TermMetadataDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TermMetadata.
 */
@RestController
@RequestMapping("/api")
public class TermMetadataResource {

    private final Logger log = LoggerFactory.getLogger(TermMetadataResource.class);

    @Inject
    private TermMetadataService termMetadataService;

    /**
     * POST  /term-metadata : Create a new termMetadata.
     *
     * @param termMetadataDTO the termMetadataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new termMetadataDTO, or with status 400 (Bad Request) if the termMetadata has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/term-metadata")
    @Timed
    public ResponseEntity<TermMetadataDTO> createTermMetadata(@RequestBody TermMetadataDTO termMetadataDTO) throws URISyntaxException {
        log.debug("REST request to save TermMetadata : {}", termMetadataDTO);
        if (termMetadataDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "termMetadata", "idexists", "A new termMetadata cannot already have an ID")
            ).body(null);
        }
        TermMetadataDTO result = termMetadataService.save(termMetadataDTO);
        return ResponseEntity.created(new URI("/api/term-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "termMetadata", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /term-metadata : Updates an existing termMetadata.
     *
     * @param termMetadataDTO the termMetadataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated termMetadataDTO,
     * or with status 400 (Bad Request) if the termMetadataDTO is not valid,
     * or with status 500 (Internal Server Error) if the termMetadataDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/term-metadata")
    @Timed
    public ResponseEntity<TermMetadataDTO> updateTermMetadata(@RequestBody TermMetadataDTO termMetadataDTO) throws URISyntaxException {
        log.debug("REST request to update TermMetadata : {}", termMetadataDTO);
        if (termMetadataDTO.getId() == null) {
            return createTermMetadata(termMetadataDTO);
        }
        TermMetadataDTO result = termMetadataService.save(termMetadataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "termMetadata", termMetadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /term-metadata : get all the termMetadata.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of termMetadata in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/term-metadata")
    @Timed
    public ResponseEntity<List<TermMetadataDTO>> getAllTermMetadata(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TermMetadata");
        Page<TermMetadataDTO> page = termMetadataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /term-metadata/:id : get the "id" termMetadata.
     *
     * @param id the id of the termMetadataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the termMetadataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/term-metadata/{id}")
    @Timed
    public ResponseEntity<TermMetadataDTO> getTermMetadata(@PathVariable Long id) {
        log.debug("REST request to get TermMetadata : {}", id);
        TermMetadataDTO termMetadataDTO = termMetadataService.findOne(id);
        return Optional.ofNullable(termMetadataDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /term-metadata/:id : delete the "id" termMetadata.
     *
     * @param id the id of the termMetadataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/term-metadata/{id}")
    @Timed
    public ResponseEntity<Void> deleteTermMetadata(@PathVariable Long id) {
        log.debug("REST request to delete TermMetadata : {}", id);
        termMetadataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "termMetadata", id.toString())).build();
    }

}
