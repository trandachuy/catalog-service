/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/01/2017
 * Author: Huyen Lam <email:huyen.lam@mediastep.com>
 *******************************************************************************/
package com.mediastep.beecow.catalog.service.util;

import com.mediastep.beecow.catalog.domain.Term;
import com.mediastep.beecow.catalog.repository.TermRepository;
import com.mediastep.beecow.catalog.service.TermService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The class is used to collect term descendants.
 * @author huyen.lam
 */
public class TermDescendantCollector {

    private final Logger log = LoggerFactory.getLogger(TermDescendantCollector.class);

    private TermRepository termRepository;

    private Set<Term> descendants = new HashSet<>();

    private Set<Long> proceededTermIds = new HashSet<>();

    public TermDescendantCollector(TermRepository termRepository) {
        this.termRepository = termRepository;

    }

    private void doGetDescendants(Long termId) {
        if (proceededTermIds.contains(termId)) {
            return; // IMPORTANT: Skip proceeded term ID, to prevent endless-loop!
        }
        proceededTermIds.add(termId);
        List<Term> children = termRepository.findAllByParent_Id(termId, TermService.DEFAULT_ORDER_BY);
        if (!children.isEmpty()) {
            descendants.addAll(children);
            // #performance query children parallelly, be aware of transaction in unit-test may cause issue
            children.parallelStream().forEach(child -> {
                doGetDescendants(child.getId());
            });
        }
    }

    private void doGetLastDescendants(Long termId) {
        Term term = termRepository.findById(termId).orElse(null);
        if (term != null) {
            doGetLastDescendants(term);
        }
    }

    /**
     * Get descendants of given term.
     * @param term term to find descendants
     */
    private void doGetLastDescendants(Term term) {
        Long termId = term.getId();
        if (proceededTermIds.contains(termId)) {
            return; // IMPORTANT: Skip proceeded term ID, to prevent endless-loop!
        }
        proceededTermIds.add(termId);
        List<Term> children = termRepository.findAllByParent_Id(termId, TermService.DEFAULT_ORDER_BY);
        if (children.isEmpty()) {
            descendants.add(term);
        }
        else {
            // #performance query children parallelly, be aware of transaction in unit-test may cause issue
            children.parallelStream().forEach(child -> {
                doGetLastDescendants(child.getId());
            });
        }
    }

    /**
     * Get descendants of given term.
     * @param termId term ID
     * @return descendants
     */
    public Set<Term> getDescendants(Long termId) {
        log.debug("Request to get all descendants of term with ID '{}'", termId);
        doGetDescendants(termId);
        return descendants;
    }

    /**
     * Get descendants of given term IDs.
     * @param termIds term IDs
     * @return descendants
     */
    public Set<Term> getDescendants(List<Long> termIds) {
        log.debug("Request to get all descendants of terms with IDs '{}'", termIds);
        for (Long termId : termIds) {
            doGetDescendants(termId);
        }
        return descendants;
    }

    /**
     * Get descendants of given term IDs.
     * @param termIds term IDs
     * @return descendants
     */
    public Set<Term> getLastDescendants(List<Long> termIds) {
        log.debug("Request to get last descendants of terms with IDs '{}'", termIds);
        for (Long termId : termIds) {
            doGetLastDescendants(termId);
        }
        return descendants;
    }
}
