/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.service.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.mediastep.beecow.catalog.domain.Term;

/**
 * Mapper for the entity Term and its DTO TermDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TermIdMapper {

    default Long termToLong(Term term) {
        return term.getId();
    }

    List<Long> termsToLongs(Set<Term> terms);
}
