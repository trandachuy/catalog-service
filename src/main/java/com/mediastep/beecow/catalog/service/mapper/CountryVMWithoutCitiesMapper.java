package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.Country;
import com.mediastep.beecow.catalog.domain.Image;
import com.mediastep.beecow.catalog.web.rest.vm.CountryVM;
import com.mediastep.beecow.common.dto.ImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Country and its DTO CountryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryVMWithoutCitiesMapper {

    @Mapping(target = "flagEmoji", source = "flag.emoji")
    @Mapping(target = "cities", ignore = true)
    CountryVM countryToCountryDTO(Country country);

    List<CountryVM> countriesToCountryDTOs(List<Country> countries);

    @Mapping(target = "cities", ignore = true)
    Country countryDTOToCountry(CountryVM countryDTO);

    List<Country> countryDTOsToCountries(List<CountryVM> countryDTOs);
    
    ImageDTO map(Image image); 
    Image map(ImageDTO imageDTO);
}
