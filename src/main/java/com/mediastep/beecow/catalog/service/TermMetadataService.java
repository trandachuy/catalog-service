/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/01/2017
 * Author: Huyen Lam <email:huyen.lam@mediastep.com>
 *******************************************************************************/
package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.domain.TermMetadata;
import com.mediastep.beecow.catalog.repository.TermMetadataRepository;
import com.mediastep.beecow.catalog.service.mapper.TermMetadataMapper;
import com.mediastep.beecow.common.dto.TermMetadataDTO;
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
 * Service Implementation for managing TermMetadata.
 */
@Service
@Transactional
public class TermMetadataService {

    private final Logger log = LoggerFactory.getLogger(TermMetadataService.class);

    @Inject
    private TermMetadataRepository termMetadataRepository;

    @Inject
    private TermMetadataMapper termMetadataMapper;

    /**
     * Save a termMetadata.
     *
     * @param termMetadataDTO the entity to save
     * @return the persisted entity
     */
    @Caching(evict = {@CacheEvict(cacheNames = "terms", allEntries = true), @CacheEvict(cacheNames = "terms-tree", allEntries = true)})
    public TermMetadataDTO save(TermMetadataDTO termMetadataDTO) {
        log.debug("Request to save TermMetadata : {}", termMetadataDTO);
        TermMetadata termMetadata = termMetadataMapper.termMetadataDTOToTermMetadata(termMetadataDTO);
        termMetadata = termMetadataRepository.save(termMetadata);
        TermMetadataDTO result = termMetadataMapper.termMetadataToTermMetadataDTO(termMetadata);
        return result;
    }

    /**
     *  Get all the termMetadata.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TermMetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TermMetadata");
        Page<TermMetadata> result = termMetadataRepository.findAll(pageable);
        return result.map(termMetadata -> termMetadataMapper.termMetadataToTermMetadataDTO(termMetadata));
    }

    /**
     *  Get one termMetadata by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TermMetadataDTO findOne(Long id) {
        log.debug("Request to get TermMetadata : {}", id);
        TermMetadata termMetadata = termMetadataRepository.findById(id).orElse(null);
        TermMetadataDTO termMetadataDTO = termMetadataMapper.termMetadataToTermMetadataDTO(termMetadata);
        return termMetadataDTO;
    }

    /**
     *  Delete the  termMetadata by id.
     *
     *  @param id the id of the entity
     */
    @Caching(evict = {@CacheEvict(cacheNames = "terms", allEntries = true), @CacheEvict(cacheNames = "terms-tree", allEntries = true)})
    public void delete(Long id) {
        log.debug("Request to delete TermMetadata : {}", id);
        termMetadataRepository.deleteById(id);
    }
}
