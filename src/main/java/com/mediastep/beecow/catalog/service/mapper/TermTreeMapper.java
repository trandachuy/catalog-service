/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.service.mapper;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Term and its DTO TermDTO.
 */
@Mapper(componentModel = "spring", uses = {TermTreeMapperProcessor.class})
public interface TermTreeMapper extends TermMapper {
}
