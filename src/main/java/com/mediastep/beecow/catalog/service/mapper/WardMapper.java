package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.Ward;
import com.mediastep.beecow.catalog.dto.WardNewDTO;
import com.mediastep.beecow.catalog.service.dto.WardValidationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Ward and its DTO WardNewDTO.
 */
@Mapper(componentModel = "spring", uses = {DistrictMapper.class})
public interface WardMapper extends EntityMapper<WardNewDTO, Ward> {

    @Mapping(source = "district.id", target = "districtId")
    WardNewDTO toDto(Ward ward);

    @Mapping(source = "districtId", target = "district")
    Ward toEntity(WardNewDTO wardDTO);

    default Ward fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ward ward = new Ward();
        ward.setId(id);
        return ward;
    }

    WardNewDTO toDto(WardValidationDTO wardValidationDTO);
}
