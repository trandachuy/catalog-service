/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/01/2017
 * Author: Huyen Lam <email:huyen.lam@mediastep.com>
 *******************************************************************************/
package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.domain.TermEntity;
import com.mediastep.beecow.catalog.repository.TermEntityRepository;
import com.mediastep.beecow.catalog.service.mapper.TermEntityMapper;
import com.mediastep.beecow.common.dto.TermEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing TermEntity.
 */
@Service
@Transactional
public class TermEntityService {

    private final Logger log = LoggerFactory.getLogger(TermEntityService.class);

    @Inject
    private TermEntityRepository termEntityRepository;

    @Inject
    private TermEntityMapper termEntityMapper;

    /**
     * Save a termEntity.
     *
     * @param termEntityDTO the entity to save
     * @return the persisted entity
     */
    public TermEntityDTO save(TermEntityDTO termEntityDTO) {
        log.debug("Request to save TermEntity : {}", termEntityDTO);
        TermEntity termEntity = termEntityMapper.termEntityDTOToTermEntity(termEntityDTO);
        termEntity = termEntityRepository.save(termEntity);
        TermEntityDTO result = termEntityMapper.termEntityToTermEntityDTO(termEntity);
        return result;
    }

    /**
     *  Get all the termEntities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TermEntityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TermEntities");
        Page<TermEntity> result = termEntityRepository.findAll(pageable);
        return result.map(termEntity -> termEntityMapper.termEntityToTermEntityDTO(termEntity));
    }

    /**
     *  Get one termEntity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TermEntityDTO findOne(Long id) {
        log.debug("Request to get TermEntity : {}", id);
        TermEntity termEntity = termEntityRepository.findById(id).orElse(null);
        TermEntityDTO termEntityDTO = termEntityMapper.termEntityToTermEntityDTO(termEntity);
        return termEntityDTO;
    }

    /**
     *  Delete the  termEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TermEntity : {}", id);
        termEntityRepository.deleteById(id);
    }
}
