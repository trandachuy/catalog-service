/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.service.TaxonomyService;
import com.mediastep.beecow.common.dto.TaxonomyDTO;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Taxonomy.
 */
@RestController
@RequestMapping("/api")
public class TaxonomyResource {

    private final Logger log = LoggerFactory.getLogger(TaxonomyResource.class);

    @Inject
    private TaxonomyService taxonomyService;

    /**
     * POST  /taxonomies : Create a new taxonomy.
     *
     * @param taxonomyDTO the taxonomyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taxonomyDTO, or with status 400 (Bad Request) if the taxonomy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/taxonomies")
    @Timed
    public ResponseEntity<TaxonomyDTO> createTaxonomy(@Valid @RequestBody TaxonomyDTO taxonomyDTO) throws URISyntaxException {
        log.debug("REST request to save Taxonomy : {}", taxonomyDTO);
        if (taxonomyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "taxonomy", "idexists", "A new taxonomy cannot already have an ID")
            ).body(null);
        }
        TaxonomyDTO result = taxonomyService.save(taxonomyDTO);
        return ResponseEntity.created(new URI("/api/taxonomies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "taxonomy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taxonomies : Updates an existing taxonomy.
     *
     * @param taxonomyDTO the taxonomyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taxonomyDTO,
     * or with status 400 (Bad Request) if the taxonomyDTO is not valid,
     * or with status 500 (Internal Server Error) if the taxonomyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/taxonomies")
    @Timed
    public ResponseEntity<TaxonomyDTO> updateTaxonomy(@Valid @RequestBody TaxonomyDTO taxonomyDTO) throws URISyntaxException {
        log.debug("REST request to update Taxonomy : {}", taxonomyDTO);
        if (taxonomyDTO.getId() == null) {
            return createTaxonomy(taxonomyDTO);
        }
        TaxonomyDTO result = taxonomyService.save(taxonomyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "taxonomy", taxonomyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taxonomies : get all the taxonomies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taxonomies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/taxonomies")
    @Timed
    public ResponseEntity<List<TaxonomyDTO>> getAllTaxonomies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Taxonomies");
        Page<TaxonomyDTO> page = taxonomyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /taxonomies/:id : get the "id" taxonomy.
     *
     * @param id the id of the taxonomyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taxonomyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/taxonomies/{id}")
    @Timed
    public ResponseEntity<TaxonomyDTO> getTaxonomy(@PathVariable Long id) {
        log.debug("REST request to get Taxonomy : {}", id);
        TaxonomyDTO taxonomyDTO = taxonomyService.findOne(id);
        return Optional.ofNullable(taxonomyDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taxonomies/:id : delete the "id" taxonomy.
     *
     * @param id the id of the taxonomyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/taxonomies/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaxonomy(@PathVariable Long id) {
        log.debug("REST request to delete Taxonomy : {}", id);
        taxonomyService.delete(id);
        return ResponseEntity.ok().headers(
            HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "taxonomy", id.toString())
        ).build();
    }

}
