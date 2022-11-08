package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;

import com.mediastep.beecow.catalog.dto.CitySimpleDTO;
import com.mediastep.beecow.common.dto.CityDTO;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity City and its DTO CityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CityMapper {

    @Mapping(source = "country.id", target = "countryId")
    CityDTO cityToCityDTO(City city);

    List<CityDTO> citiesToCityDTOs(List<City> cities);

    @Mapping(source = "countryId", target = "country")
    @Mapping(target = "districts", ignore = true)
    City cityDTOToCity(CityDTO cityDTO);

    List<City> cityDTOsToCities(List<CityDTO> cityDTOs);

    CitySimpleDTO cityToCitySimpleDTO(City city);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
