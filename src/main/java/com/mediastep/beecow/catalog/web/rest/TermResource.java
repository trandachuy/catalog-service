/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.client.StoreCatalogServiceClient;
import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.domain.util.EntityUtil;
import com.mediastep.beecow.catalog.dto.TermDTO;
import com.mediastep.beecow.catalog.service.TermService;
import com.mediastep.beecow.common.security.AuthoritiesConstants;
import com.mediastep.beecow.store.service.dto.StoreDTO;
import feign.FeignException;
import io.github.jhipster.web.util.HeaderUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Term.
 */
@RestController
@RequestMapping("/api")
public class TermResource {

    private final Logger log = LoggerFactory.getLogger(TermResource.class);

    private static final String ENTITY_NAME = "catalogTerm";

    @Inject
    private TermService termService;

    @Inject
    private StoreCatalogServiceClient storeCatalogServiceClient;

    /**
     * POST  /terms : Create a new term.
     *
     * @param termDTO the termDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new termDTO, or with status 400 (Bad Request) if the term has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER, AuthoritiesConstants.EDITOR})
    @PostMapping("/terms")
    @Timed
    public ResponseEntity<TermDTO> createTerm(@Valid @RequestBody TermDTO termDTO, @RequestParam(required = false) String taxonomy) throws URISyntaxException {
        log.debug("REST request to save Term : {}", termDTO);
        if (termDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "term", "idexists", "A new term cannot already have an ID")
            ).body(null);
        }
        TermDTO result = termService.save(termDTO, taxonomy);
        return ResponseEntity.created(new URI("/api/terms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "term", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /terms : Updates an existing term.
     *
     * @param termDTO the termDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated termDTO,
     * or with status 400 (Bad Request) if the termDTO is not valid,
     * or with status 500 (Internal Server Error) if the termDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER, AuthoritiesConstants.EDITOR})
    @PutMapping("/terms")
    @Timed
    public ResponseEntity<TermDTO> updateTerm(@Valid @RequestBody TermDTO termDTO, @RequestParam(required = false) String taxonomy) throws URISyntaxException {
        log.debug("REST request to update Term : {}", termDTO);
        if (termDTO.getId() == null) {
            return createTerm(termDTO, taxonomy);
        }
        TermDTO result = termService.save(termDTO, taxonomy);
        termService.updateDescendantsTermLevels(result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "term", termDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /terms/tree : get all the terms as trees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/terms/tree")
    @Timed
    @Caching(cacheable = {
        @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_AND_CHILDREN_BY_TAXONOMY_AND_METADATA, key = "#root.methodName", condition = "#p0 == null && #p1 == null"),
        @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_AND_CHILDREN_BY_TAXONOMY_AND_METADATA, key = "#p0", condition = "#p0 != null && #p1 == null"),
        @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_AND_CHILDREN_BY_TAXONOMY_AND_METADATA, key = "{ #p0, #p1 }", condition = "#p0 != null && #p1 != null")
    })
    public List<TermDTO> getAllTermsAndChidren(@RequestParam(required = false) String taxonomy, @RequestParam(required = false) String metadata) {
        // TODO support pagination to reduce query time on big tree
        log.debug("REST request to get all first level Terms and their chidren");
        List<TermDTO> result = termService.getAllTermsAndChidren(taxonomy, EntityUtil.stringToMap(metadata));
        return result;
    }

    /**
     * GET  /terms?ids=:ids&taxonomy=:taxonomy&parentId=:parentId&level=:level&metadata=:key=:value : get all the terms.
     *
     * @param ids      the ids
     * @param taxonomy the taxonomy
     * @param parentId the parent id
     * @param level    the level
     * @param metadata the metadata
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/terms")
    @Timed
    @Caching(cacheable = {
        @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_BY_TAXONOMY_AND_LEVEL, key = "#root.methodName", condition = "#p1 == null && #p3 == null && #p2 == null"),
        @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_BY_TAXONOMY_AND_LEVEL, key = "#p1", condition = "#p1 != null && #p3 == null && #p2 == null"),
        @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_BY_TAXONOMY_AND_LEVEL, key = "{ #p2, #p3 }", condition = "#p1 == null && #p2 != null && #p3 != null "),
        @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_BY_TAXONOMY_AND_LEVEL, key = "{ #p1, #p2, #p3 }", condition = "#p1 != null && #p2 != null && #p3 != null ")
    })
    public List<TermDTO> getAllTerms(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(required = false) String taxonomy,
                                     @RequestParam(required = false) Long parentId,
                                     @RequestParam(required = false) Integer level,
                                     @RequestParam(required = false) String metadata) {
        log.debug("REST request to get a page of Terms");
        List<TermDTO> result;
        if ((ids == null || ids.isEmpty()) && taxonomy == null && parentId == null && level == null && metadata == null) {
            result = termService.findAll();
        }
        else {
            result = termService.findAll(ids, taxonomy, parentId, level, EntityUtil.stringToMap(metadata));
        }
        return result;
    }

    /**
     * GET  /terms/:id : get the "id" term.
     *
     * @param id the id of the termDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the termDTO, or with status 404 (Not Found)
     */
    @GetMapping("/terms/{id}")
    @Timed
    public ResponseEntity<TermDTO> getTerm(@PathVariable Long id) {
        log.debug("REST request to get Term : {}", id);
        TermDTO termDTO = termService.findOne(id);
        return Optional.ofNullable(termDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /terms/{id}/children : get all children terms.
     *
     * @param id term ID
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/terms/{id}/children")
    @Timed
    public List<TermDTO> getChildren(@PathVariable Long id) {
        log.debug("REST request to get all children by term");
        return termService.findChildren(id);
    }

    /**
     * GET  /terms/{id}/tree : get all the terms as trees.
     *
     * @param id term ID
     * @return the ResponseEntity with status 200 (OK) and the term
     */
    @GetMapping("/terms/{id}/tree")
    @Timed
    public ResponseEntity<TermDTO> getAllTermsAndChidren(@PathVariable Long id) {
        // TODO support pagination to reduce query time on big tree
        log.debug("REST request to get Term and it children : {}", id);
        TermDTO termDTO = termService.getAllTermsAndChidren(id);
        return Optional.ofNullable(termDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /terms/:id : delete the "id" term.
     *
     * @param id the id of the termDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @DeleteMapping("/terms/{id}")
    @Timed
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        log.debug("REST request to delete Term : {}", id);
        termService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "term", id.toString())).build();
    }

    /**
     * GET  get all children of parent term id.
     *
     * @param parentIds parent term IDs
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/terms/list/parent-id")
    @Timed
    public ResponseEntity<List<TermDTO>> getTermByListParentId(@ApiParam @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable,
                                                               @RequestParam("parentIds") List<Long> parentIds) {
        log.debug("REST request to get all children by term");
        List<TermDTO> result =  termService.getTermByListParentId(parentIds, pageable);
        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }

    /**
     * GET  get all term by list parent id and id.
     *
     * @param ids term IDs
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/terms/list/parent-id/id")
    @Timed
    public ResponseEntity<List<TermDTO>> getTermByListParentIdAndId(@RequestParam("ids") List<Long> ids,
                                                                    @RequestParam("parentIds") List<Long> parentIds) {
        log.debug("REST request to get multiple term");
        List<TermDTO> result =  termService.getTermByListParentIdAndId(parentIds, ids);
        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }


    /**
     * GET  /terms?ids=:ids&taxonomy=:taxonomy&parentId=:parentId&level=:level&metadata=:key=:value : get term tree by id list.
     *
     * @param ids      the ids
     * @param taxonomy the taxonomy
     * @param metadata the metadata
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/terms/ids")
    @Timed
    public List<TermDTO> getAllTermsByIds(@RequestParam(required = false) List<Long> ids, @RequestParam(required = false) String taxonomy, @RequestParam(required = false) String metadata) {
        log.debug("REST request to get a page of Terms");
        List<TermDTO> result;
        result = termService.findAllByIds(ids, taxonomy, EntityUtil.stringToMap(metadata));
        return result;
    }


    /**
     * GET /terms/tree/store-ids/:storeId : get term tree by storeId
     *
     * @param storeId
     * @param taxonomy
     * @param metadata
     * @return
     */
    @GetMapping("/terms/tree/store-ids/{storeId}")
    @Timed
    public ResponseEntity<List<TermDTO>> getTermTreeByStoreId(HttpServletRequest request, @PathVariable("storeId") Long storeId, @RequestParam(required = false) String taxonomy, @RequestParam(required = false) String metadata) {
        log.debug("REST request to get tree by storeId");
        try {
            String jwt = request.getHeader("Authorization");
            ResponseEntity<StoreDTO> storeRes = storeCatalogServiceClient.getStoreById(jwt, storeId);
            StoreDTO storeDTO = storeRes.getBody();
            if (storeDTO.getCategoryIds().isEmpty()) {
                storeDTO.setCategoryIds(null);
            }
            List<TermDTO> termTree = termService.findAllByIds(storeDTO.getCategoryIds(), taxonomy, EntityUtil.stringToMap(metadata));

            return ResponseEntity.ok(termTree);
        } catch (FeignException | JpaObjectRetrievalFailureException e) {
            if ( e.getMessage().contains("status 404") ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(
                        HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "storeService", "err.storeNotFound", "err.storeNotFound")
                    )
                    .body(null);
            }
            if (e.getMessage().contains("Unable to find")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(
                        HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, "storeNotFound", "storeNotFound")
                    )
                    .body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, e.getMessage(), e.getMessage()))
                .body(null);
        }

    }
}
