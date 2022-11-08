/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/01/2017
 * Author: Huyen Lam <email:huyen.lam@mediastep.com>
 *******************************************************************************/
package com.mediastep.beecow.catalog.service;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediastep.beecow.catalog.domain.Term;
import com.mediastep.beecow.catalog.repository.TermRepository;
import com.mediastep.beecow.catalog.service.mapper.TermIdMapper;
import com.mediastep.beecow.catalog.service.util.TermDescendantCollector;

/**
 * Service Implementation for managing Term.
 */
@Service
@Transactional
public class TermIdService {

    private final Logger log = LoggerFactory.getLogger(TermIdService.class);

    @Inject
    private TermRepository termRepository;

    @Inject
    private TermIdMapper termIdMapper;

    /**
     * Get descendants of given term IDs.
     * @param termIds term IDs to search
     * @param descendant IDs
     */
    public List<Long> getDescendants(List<Long> termIds) {
        log.debug("Request to get all descendantIDs of term with IDs '{}'", termIds);
        TermDescendantCollector descendantCollector = new TermDescendantCollector(termRepository);
        Set<Term> result = descendantCollector.getDescendants(termIds);
        return termIdMapper.termsToLongs(result);
    }

    /**
     * Get last descendants of given term IDs.
     * @param termIds term IDs to search
     * @param descendant IDs, if term has no descendants the list contains input term ID
     */
    public List<Long> getLastDescendants(List<Long> termIds) {
        log.debug("Request to get all descendantIDs of term with IDs '{}'", termIds);
        TermDescendantCollector descendantCollector = new TermDescendantCollector(termRepository);
        Set<Term> result = descendantCollector.getLastDescendants(termIds);
        return termIdMapper.termsToLongs(result);
    }
}
