/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.common.dto.TermMetadataDTO;

import org.mapstruct.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for the entity TermMetadata and its DTO TermMetadataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TermMetadataMapper {

    @Mapping(source = "term.id", target = "termId")
    TermMetadataDTO termMetadataToTermMetadataDTO(TermMetadata termMetadata);

    List<TermMetadataDTO> termMetadataToTermMetadataDTOs(List<TermMetadata> termMetadata);

    @Mapping(source = "termId", target = "term")
    TermMetadata termMetadataDTOToTermMetadata(TermMetadataDTO termMetadataDTO);

    List<TermMetadata> termMetadataDTOsToTermMetadata(List<TermMetadataDTO> termMetadataDTOs);

    default Map<String, TermMetadataDTO> map(List<TermMetadata> metadataList) {
        Map<String, TermMetadataDTO> metadataDTOList = new HashMap<>();
        for (TermMetadata metadata : metadataList) {
            TermMetadataDTO metadataDTO = termMetadataToTermMetadataDTO(metadata);
            metadataDTOList.put(metadataDTO.getMetaKey(), metadataDTO);
        }
        return metadataDTOList;
    }

    default Term termFromId(Long id) {
        if (id == null) {
            return null;
        }
        Term term = new Term();
        term.setId(id);
        return term;
    }
}
