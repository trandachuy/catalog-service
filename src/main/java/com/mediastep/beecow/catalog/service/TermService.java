/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/01/2017
 * Author: Huyen Lam <email:huyen.lam@mediastep.com>
 *******************************************************************************/
package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.domain.Taxonomy;
import com.mediastep.beecow.catalog.domain.Term;
import com.mediastep.beecow.catalog.domain.TermMetadata;
import com.mediastep.beecow.catalog.dto.TermDTO;
import com.mediastep.beecow.catalog.repository.TaxonomyRepository;
import com.mediastep.beecow.catalog.repository.TermMetadataRepository;
import com.mediastep.beecow.catalog.repository.TermRepository;
import com.mediastep.beecow.catalog.service.mapper.TermSimpleMapper;
import com.mediastep.beecow.catalog.service.mapper.TermTreeMapper;
import com.mediastep.beecow.catalog.service.util.TermDescendantCollector;
import com.mediastep.beecow.catalog.web.rest.errors.CustomParameterizedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Term.
 */
@Service
@Transactional
public class TermService {

    public static Sort DEFAULT_ORDER_BY = Sort.by("taxonomy", "parent", "termLevel", "termOrder");

    private static final int TERM_LEVEL_FIRST = 0;

    private final Logger log = LoggerFactory.getLogger(TermService.class);

    @Inject
    private TaxonomyRepository taxonomyRepository;

    @Inject
    private TermRepository termRepository;

    @Inject
    private TermSimpleMapper termMapper;

    @Inject
    private TermTreeMapper termTreeMapper;

    @Inject
    private TermMetadataRepository termMetadataRepository;

    /**
     * Save a term.
     *
     * @param termDTO the entity to save
     * @return the persisted entity
     */
    @Caching(evict = {@CacheEvict(cacheNames = "terms", allEntries = true), @CacheEvict(cacheNames = "terms-tree", allEntries = true)})
    public TermDTO save(TermDTO termDTO, String taxonomyName) {
        log.debug("Request to save Term : {}, taxonomy name='{}'", termDTO, taxonomyName);

        Term term = termMapper.termDTOToTerm(termDTO);
        if (StringUtils.isNotBlank(taxonomyName)) {
            Taxonomy taxonomy = taxonomyRepository.findOneByName(taxonomyName);
            if (taxonomy == null) {
                throw new CustomParameterizedException("termService.save.taxonomyNotFound", taxonomyName);
            }
            term.setTaxonomy(taxonomy);
        }
        term = doSaveTerm(term);

        if(!ObjectUtils.isEmpty(termDTO.getMetadata())) {
            Map<String, Object> metadata = termDTO.getMetadata();
            List<TermMetadata> termMetadatas = termMetadataRepository.findAllByTerm_Id(termDTO.getId());
            termMetadatas.stream().forEach((t) -> {
                System.out.println(metadata.get(t.getMetaKey()));
                if (!ObjectUtils.isEmpty(metadata.get(t.getMetaKey()))) {
                    String metaValue = metadata.get(t.getMetaKey()).toString();
                    t.setMetaValue(metaValue);
                    TermMetadata m = termMetadataRepository.save(t);
                }
            });
        }

        TermDTO result = termMapper.termToTermDTO(term);
        return result;
    }

    /**
     * Do save term into database
     * @param term
     */
    private Term doSaveTerm(Term term) {
        setTermLevel(term);
        term = termRepository.save(term);
        return term;
    }

    /**
     * Set term level.
     */
    private void setTermLevel(Term term) {
        Term parent = term.getParent();
        int level = TERM_LEVEL_FIRST;
        if (parent != null) {
            Optional<Term> optParent = termRepository.findById(parent.getId());
            if (optParent.isPresent()) {
                level = optParent.get().getTermLevel() + 1;
            }
        }
        term.setTermLevel(level);
    }

    /**
     * Update descendants term level accordingly.
     * @param termId term with descendants to be updated
     */
    public void updateDescendantsTermLevels(Long termId) {
        log.debug("Update term level for all descendants of Term with ID {}", termId);
        TermDescendantCollector termDescendantService = new TermDescendantCollector(termRepository);
        Set<Term> descendants = termDescendantService.getDescendants(termId);
        for (Term descendant : descendants) {
            doSaveTerm(descendant);
        }
    }

    /**
     *  Get all the terms as tree.
     *
     *  @param taxonomyName taxonomy name which terms belong to
     *  @return the list of entities
     */
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_TREE)
    @Transactional(readOnly = true)
    public List<TermDTO> getAllTermsAndChidren(String taxonomyName, Map<String, String> metadata) {
        log.debug("Request to get all first level Terms and their children with taxonomy-name='{}'", taxonomyName);
        String medaKey = null;
        String medaValue = null;
        if (metadata != null && !metadata.isEmpty()) {
            Entry<String, String> metadataEntry = metadata.entrySet().iterator().next();
            medaKey = metadataEntry.getKey();
            medaValue = metadataEntry.getValue();
        }
        List<Term> result = termRepository.findAll(TERM_LEVEL_FIRST, taxonomyName, medaKey, medaValue, DEFAULT_ORDER_BY);
        return termTreeMapper.termsToTermDTOs(result);
    }

    /**
     *  Get all the terms as tree.
     *
     *  @param id taxonomy name which terms belong to
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TermDTO getAllTermsAndChidren(Long id) {
        log.debug("Request to get Term : {}", id);
        Term term = termRepository.findById(id).orElse(null);
        return termTreeMapper.termToTermDTO(term);
    }

    /**
     *  Get all the terms.
     *
     *  @return the list of entities
     */
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM)
    @Transactional(readOnly = true)
    public List<TermDTO> findAll() {
        log.debug("Request to get all Terms");
        List<Term> result = termRepository.findAll(DEFAULT_ORDER_BY);
        return termMapper.termsToTermDTOs(result);
    }

    /**
     *  Get all the terms.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TermDTO> findAll(List<Long> ids, String taxonomy, Long parentId, Integer level, Map<String, String> metadata) {
        log.debug("Request to get all Terms");
        String medaKey = null;
        String medaValue = null;
        if (metadata != null && !metadata.isEmpty()) {
            Entry<String, String> metadataEntry = metadata.entrySet().iterator().next();
            medaKey = metadataEntry.getKey();
            medaValue = metadataEntry.getValue();
        }
        List<Term> result = termRepository.findAll(ids, taxonomy, parentId, level, medaKey, medaValue, DEFAULT_ORDER_BY);
        return termMapper.termsToTermDTOs(result);
    }

    /**
     *  Get all the terms.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_TERM_TREE)
    public List<TermDTO> findAllByIds(List<Long> ids, String taxonomy, Map<String, String> metadata) {
        log.debug("Request to get all Terms");
        String medaKey = null;
        String medaValue = null;
        if (metadata != null && !metadata.isEmpty()) {
            Entry<String, String> metadataEntry = metadata.entrySet().iterator().next();
            medaKey = metadataEntry.getKey();
            medaValue = metadataEntry.getValue();
        }
        List<Term> result = termRepository.findAllByIdList(ids, taxonomy, medaKey, medaValue, DEFAULT_ORDER_BY);
        return termTreeMapper.termsToTermDTOs(result);
    }

    /**
     *  Get all the terms.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TermDTO> findAll(ZonedDateTime fromDate) {
        log.debug("Request to get all Terms modified from-date '{}'", fromDate);
        List<Term> result = termRepository.findAllByLastModifiedDateBetween(fromDate, ZonedDateTime.now(), DEFAULT_ORDER_BY);
        return termMapper.termsToTermDTOs(result);
    }

    /**
     *  Get all the terms.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TermDTO> findAll(ZonedDateTime fromDate, String taxonomyName) {
        log.debug("Request to get all Terms with taxonomy name='{}' modified from-date '{}'", taxonomyName, fromDate);
        List<Term> result = termRepository.findAllByTaxonomy_NameAndLastModifiedDateBetween(
                taxonomyName, fromDate, ZonedDateTime.now(), DEFAULT_ORDER_BY);
        return termMapper.termsToTermDTOs(result);
    }

    /**
     *  Get all the terms.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TermDTO> findChildren(Long termId) {
        log.debug("Request to get all Terms");
        List<Term> result = termRepository.findAllByParent_Id(termId, DEFAULT_ORDER_BY);
        return termMapper.termsToTermDTOs(result);
    }

    /**
     *  Get one term by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TermDTO findOne(Long id) {
        log.debug("Request to get Term : {}", id);
        Term term = termRepository.findById(id).orElse(null);
        return termMapper.termToTermDTO(term);
    }

    /**
     *  Delete the  term by id.
     *
     *  @param id the id of the entity
     */
    @Caching(evict = {@CacheEvict(cacheNames = "terms", allEntries = true), @CacheEvict(cacheNames = "terms-tree", allEntries = true)})
    public void delete(Long id) {
        log.debug("Request to delete Term : {}", id);
        termRepository.deleteById(id);
    }

    /**
     * Get term by list parent id. Sort by parent id asc and term order asc
     * @param parentIds list of id
     * @param pageable for paging
     * @return the list of term
     */
    public List<TermDTO> getTermByListParentId(List<Long> parentIds, Pageable pageable) {
        log.debug("Request to get term by list parent id: {}", parentIds);
        Page<Term> result = termRepository.findByParentIdIn(parentIds, pageable);
        return termMapper.termsToTermDTOs(result.getContent());
    }

    /**
     * Get term by list parent id, id
     * @param parentIds list of parent id
     * @param ids list of id
     * @return the list of term
     */
    public List<TermDTO> getTermByListParentIdAndId(List<Long> parentIds, List<Long> ids) {
        log.debug("Request to get term by list id: {}", ids);
        Pageable pageable = PageRequest.of(0 , Integer.MAX_VALUE, Sort.Direction.ASC, "parentId", "termOrder", "id");
        Page<Term> result = termRepository.findByParentIdInAndIdIn(parentIds, ids, pageable);
        return termMapper.termsToTermDTOs(result.getContent());
    }
}
