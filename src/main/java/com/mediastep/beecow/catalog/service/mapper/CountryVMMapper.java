package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.City;
import com.mediastep.beecow.catalog.domain.Country;
import com.mediastep.beecow.catalog.domain.Image;
import com.mediastep.beecow.catalog.web.rest.vm.CountryVM;
import com.mediastep.beecow.common.dto.CityDTO;
import com.mediastep.beecow.common.dto.CountryDTO;
import com.mediastep.beecow.common.dto.ImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper for the entity Country and its DTO CountryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryVMMapper {

    @Mapping(target = "flagEmoji", source = "flag.emoji")
    @Mapping(target = "cities", qualifiedByName = "citieMapper")
    CountryVM countryToCountryDTO(Country country);

    @Named("citieMapper")
    @Mapping(target = "districts", ignore = true)
    CityDTO entityToDto(City city);

    List<CountryVM> countriesToCountryDTOs(List<Country> countries);

    @Mapping(target = "cities", ignore = true)
    Country countryDTOToCountry(CountryVM countryDTO);

    List<Country> countryDTOsToCountries(List<CountryVM> countryDTOs);

    ImageDTO map(Image image); 
    Image map(ImageDTO imageDTO);

    @Mapping(target = "districts", ignore = true)
    CityDTO map(City city);

    @Mapping(target = "districts", ignore = true)
    City map(CityDTO cityDTO);
}
