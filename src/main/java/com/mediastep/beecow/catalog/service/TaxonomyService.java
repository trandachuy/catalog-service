/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/01/2017
 * Author: Huyen Lam <email:huyen.lam@mediastep.com>
 *******************************************************************************/
package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.domain.Taxonomy;
import com.mediastep.beecow.catalog.repository.TaxonomyRepository;
import com.mediastep.beecow.catalog.service.mapper.TaxonomyMapper;
import com.mediastep.beecow.common.dto.TaxonomyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing Taxonomy.
 */
@Service
@Transactional
public class TaxonomyService {

    private final Logger log = LoggerFactory.getLogger(TaxonomyService.class);

    @Inject
    private TaxonomyRepository taxonomyRepository;

    @Inject
    private TaxonomyMapper taxonomyMapper;

    /**
     * Save a taxonomy.
     *
     * @param taxonomyDTO the entity to save
     * @return the persisted entity
     */
    @Caching(evict = {@CacheEvict(cacheNames = "terms", allEntries = true), @CacheEvict(cacheNames = "terms-tree", allEntries = true)})
    public TaxonomyDTO save(TaxonomyDTO taxonomyDTO) {
        log.debug("Request to save Taxonomy : {}", taxonomyDTO);
        Taxonomy taxonomy = taxonomyMapper.taxonomyDTOToTaxonomy(taxonomyDTO);
        taxonomy = taxonomyRepository.save(taxonomy);
        TaxonomyDTO result = taxonomyMapper.taxonomyToTaxonomyDTO(taxonomy);
        return result;
    }

    /**
     *  Get all the taxonomies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaxonomyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Taxonomies");
        Page<Taxonomy> result = taxonomyRepository.findAll(pageable);
        return result.map(taxonomy -> taxonomyMapper.taxonomyToTaxonomyDTO(taxonomy));
    }

    /**
     *  Get one taxonomy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TaxonomyDTO findOne(Long id) {
        log.debug("Request to get Taxonomy : {}", id);
        Taxonomy taxonomy = taxonomyRepository.findById(id).orElse(null);
        TaxonomyDTO taxonomyDTO = taxonomyMapper.taxonomyToTaxonomyDTO(taxonomy);
        return taxonomyDTO;
    }

    /**
     *  Delete the  taxonomy by id.
     *
     *  @param id the id of the entity
     */
    @Caching(evict = {@CacheEvict(cacheNames = "terms", allEntries = true), @CacheEvict(cacheNames = "terms-tree", allEntries = true)})
    public void delete(Long id) {
        log.debug("Request to delete Taxonomy : {}", id);
        taxonomyRepository.deleteById(id);
    }
}
