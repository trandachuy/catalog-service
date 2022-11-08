/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.service.TermEntityService;
import com.mediastep.beecow.common.dto.TermEntityDTO;
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
 * REST controller for managing TermEntity.
 */
@RestController
@RequestMapping("/api")
public class TermEntityResource {

    private final Logger log = LoggerFactory.getLogger(TermEntityResource.class);

    @Inject
    private TermEntityService termEntityService;

    /**
     * POST  /term-entities : Create a new termEntity.
     *
     * @param termEntityDTO the termEntityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new termEntityDTO, or with status 400 (Bad Request) if the termEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/term-entities")
    @Timed
    public ResponseEntity<TermEntityDTO> createTermEntity(@RequestBody TermEntityDTO termEntityDTO) throws URISyntaxException {
        log.debug("REST request to save TermEntity : {}", termEntityDTO);
        if (termEntityDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "termEntity", "idexists", "A new termEntity cannot already have an ID")
            ).body(null);
        }
        TermEntityDTO result = termEntityService.save(termEntityDTO);
        return ResponseEntity.created(new URI("/api/term-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "termEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /term-entities : Updates an existing termEntity.
     *
     * @param termEntityDTO the termEntityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated termEntityDTO,
     * or with status 400 (Bad Request) if the termEntityDTO is not valid,
     * or with status 500 (Internal Server Error) if the termEntityDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/term-entities")
    @Timed
    public ResponseEntity<TermEntityDTO> updateTermEntity(@RequestBody TermEntityDTO termEntityDTO) throws URISyntaxException {
        log.debug("REST request to update TermEntity : {}", termEntityDTO);
        if (termEntityDTO.getId() == null) {
            return createTermEntity(termEntityDTO);
        }
        TermEntityDTO result = termEntityService.save(termEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "termEntity", termEntityDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /term-entities : get all the termEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of termEntities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/term-entities")
    @Timed
    public ResponseEntity<List<TermEntityDTO>> getAllTermEntities(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TermEntities");
        Page<TermEntityDTO> page = termEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /term-entities/:id : get the "id" termEntity.
     *
     * @param id the id of the termEntityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the termEntityDTO, or with status 404 (Not Found)
     */
    @GetMapping("/term-entities/{id}")
    @Timed
    public ResponseEntity<TermEntityDTO> getTermEntity(@PathVariable Long id) {
        log.debug("REST request to get TermEntity : {}", id);
        TermEntityDTO termEntityDTO = termEntityService.findOne(id);
        return Optional.ofNullable(termEntityDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /term-entities/:id : delete the "id" termEntity.
     *
     * @param id the id of the termEntityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/term-entities/{id}")
    @Timed
    public ResponseEntity<Void> deleteTermEntity(@PathVariable Long id) {
        log.debug("REST request to delete TermEntity : {}", id);
        termEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "termEntity", id.toString())).build();
    }


}
