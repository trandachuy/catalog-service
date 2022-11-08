/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.common.dto.TaxonomyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Taxonomy and its DTO TaxonomyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaxonomyMapper {

    TaxonomyDTO taxonomyToTaxonomyDTO(Taxonomy taxonomy);

    List<TaxonomyDTO> taxonomiesToTaxonomyDTOs(List<Taxonomy> taxonomies);

    @Mapping(target = "terms", ignore = true)
    Taxonomy taxonomyDTOToTaxonomy(TaxonomyDTO taxonomyDTO);

    List<Taxonomy> taxonomyDTOsToTaxonomies(List<TaxonomyDTO> taxonomyDTOs);
}
