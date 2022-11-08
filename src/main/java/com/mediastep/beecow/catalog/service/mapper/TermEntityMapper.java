/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.common.dto.TermEntityDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TermEntity and its DTO TermEntityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TermEntityMapper {

    @Mapping(source = "term.id", target = "termId")
    TermEntityDTO termEntityToTermEntityDTO(TermEntity termEntity);

    List<TermEntityDTO> termEntitiesToTermEntityDTOs(List<TermEntity> termEntities);

    @Mapping(source = "termId", target = "term")
    TermEntity termEntityDTOToTermEntity(TermEntityDTO termEntityDTO);

    List<TermEntity> termEntityDTOsToTermEntities(List<TermEntityDTO> termEntityDTOs);

    default Term termFromId(Long id) {
        if (id == null) {
            return null;
        }
        Term term = new Term();
        term.setId(id);
        return term;
    }
}
