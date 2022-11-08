/*******************************************************************************
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 26/1/2017
 * Author: Huyen Lam <huyen.lam@mediastep.com>
 *******************************************************************************/
package com.mediastep.beecow.catalog.service.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import com.mediastep.beecow.catalog.domain.Term;
import com.mediastep.beecow.catalog.repository.TermRepository;
import com.mediastep.beecow.catalog.dto.TermDTO;

/**
 * The class maps Term to TermDTO.
 */
@Component
public class TermTreeMapperProcessor {

    @Inject
    private TermRepository termRepository;

    @Inject
    private TermTreeMapper termTreeMapper;

    private Set<Long> mapChildIDs = new HashSet<>();

    @BeforeMapping
    public void beforeMapChildren(Term source, @MappingTarget TermDTO target) {
        mapChildIDs.clear();
    }

    @AfterMapping
    public void mapChildren(Term source, @MappingTarget TermDTO target) {
        Long id = source.getId();
        if (mapChildIDs.contains(id)) {
            return; // IMPORTANT: Skip proceeded term, to prevent endless-loop!
        }
        else {
            mapChildIDs.add(id);
        }
        List<Term> children = termRepository.findAllByParent_Id(source.getId(), null);
        List<TermDTO> childrenDTOs = termTreeMapper.termsToTermDTOs(children);
        target.setChildren(childrenDTOs);
    }
}
