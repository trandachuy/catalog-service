package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.City;
import com.mediastep.beecow.catalog.domain.Country;
import com.mediastep.beecow.catalog.domain.Image;
import com.mediastep.beecow.catalog.dto.CountrySimpleDTO;
import com.mediastep.beecow.common.dto.CityDTO;
import com.mediastep.beecow.common.dto.CountryDTO;
import com.mediastep.beecow.common.dto.ImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Country and its DTO CountryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryMapper {

    CountryDTO countryToCountryDTO(Country country);

    CountrySimpleDTO countryDTOToCountrySimpleDTO(CountryDTO countryDTO);

    List<CountryDTO> countriesToCountryDTOs(List<Country> countries);

    @Mapping(target = "cities", ignore = true)
    Country countryDTOToCountry(CountryDTO countryDTO);

    List<Country> countryDTOsToCountries(List<CountryDTO> countryDTOs);
    
    ImageDTO map(Image image); 
    Image map(ImageDTO imageDTO);
    
    CityDTO map(City city); 
    City map(CityDTO cityDTO);
}
