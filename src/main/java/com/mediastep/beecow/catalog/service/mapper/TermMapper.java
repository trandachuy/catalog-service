/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.service.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapping;

import com.mediastep.beecow.catalog.domain.Taxonomy;
import com.mediastep.beecow.catalog.domain.Term;
import com.mediastep.beecow.catalog.domain.TermMetadata;
import com.mediastep.beecow.catalog.domain.TermMetadataKeys;
import com.mediastep.beecow.common.dto.ImageDTO;
import com.mediastep.beecow.common.dto.TaxonomyDTO;
import com.mediastep.beecow.catalog.dto.TermDTO;
import com.mediastep.beecow.common.dto.TermMetadataDTO;
import com.mediastep.beecow.common.util.ImageDtoUtil;

/**
 * Mapper for the entity Term and its DTO TermDTO.
 */
public interface TermMapper {

    @Mapping(source = "taxonomy.id", target = "taxonomyId")
    @Mapping(source = "taxonomy.name", target = "taxonomy")
    @Mapping(source = "parent.id", target = "parentId")
	TermDTO termToTermDTO(Term term);

	default List<TermDTO> termsToTermDTOs(List<Term> terms) {
	    // #performance
	    return terms.parallelStream().map(this::termToTermDTO).collect(Collectors.toList());
	}

    @Mapping(source = "taxonomyId", target = "taxonomy")
    @Mapping(source = "taxonomy", target = "taxonomy.name")
    @Mapping(source = "parentId", target = "parent")
    @Mapping(target = "metadata", ignore = true)
    Term termDTOToTerm(TermDTO termDTO);

    List<Term> termDTOsToTerms(List<TermDTO> termDTOs);

    @Mapping(source = "term.id", target = "termId")
    TermMetadataDTO map(TermMetadata metadata);

    TaxonomyDTO map(Taxonomy taxonomy);

    @Mapping(target = "terms", ignore = true)
    Taxonomy map(TaxonomyDTO taxonomyDTO);

    default Map<String, Object> map(Collection<TermMetadata> metadataList) {
        Map<String, Object> metadataDTOList = new HashMap<>();
        for (TermMetadata metadata : metadataList) {
            TermMetadataDTO metadataDTO = map(metadata);
            String metadataKey = metadataDTO.getMetaKey();
            String metadataValue = metadataDTO.getMetaValue();
            if (metadataKey.equals(TermMetadataKeys.ICON)) {
                ImageDTO imageDTO = ImageDtoUtil.stringToImageDTO(metadataValue);
                metadataDTOList.put(metadataKey, imageDTO);
            }
            else {
                metadataDTOList.put(metadataKey, metadataValue);
            }
        }
        return metadataDTOList;
    }

    default Taxonomy taxonomyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(id);
        return taxonomy;
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
