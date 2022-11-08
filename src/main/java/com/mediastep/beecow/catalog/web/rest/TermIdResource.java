/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.service.TermIdService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Term.
 */
@RestController
@RequestMapping("/api")
public class TermIdResource {

    private final Logger log = LoggerFactory.getLogger(TermIdResource.class);

    @Inject
    private TermIdService termIdService;

    /**
     * GET  /term-ids/descendants?ids=:id1,:di2 : get all descendants IDs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/term-ids/descendants")
    @Timed
    public List<Long> getDescendants(@RequestParam List<Long> ids)
        throws URISyntaxException {
        log.debug("REST request to get all descendants IDs");
        return termIdService.getDescendants(ids);
    }

    /**
     * GET  /term-ids/last-descendants?ids=:id1,:di2 : get all descendants IDs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/term-ids/last-descendants")
    @Timed
    public List<Long> getLastDescendants(@RequestParam List<Long> ids)
        throws URISyntaxException {
        log.debug("REST request to get all descendants IDs");
        return termIdService.getLastDescendants(ids);
    }
}
