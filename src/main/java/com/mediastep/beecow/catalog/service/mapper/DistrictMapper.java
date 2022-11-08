package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.District;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity District and its DTO DistrictDTO.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class})
public interface DistrictMapper extends EntityMapper<DistrictDTO, District> {

    @Mapping(source = "city.id", target = "cityId")
    DistrictDTO toDto(District entity);

    default District fromId(Long id) {
        if (id == null) {
            return null;
        }
        District district = new District();
        district.setId(id);
        return district;
    }

    DistrictDTO toDto(DistrictValidationDTO districtValidationDTO);

}
