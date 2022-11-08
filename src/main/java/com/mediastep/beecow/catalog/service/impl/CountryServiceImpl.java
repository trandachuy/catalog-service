package com.mediastep.beecow.catalog.service.impl;

import com.google.common.collect.Lists;
import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.domain.City;
import com.mediastep.beecow.catalog.domain.Country;
import com.mediastep.beecow.catalog.domain.District;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.dto.WardNewDTO;
import com.mediastep.beecow.catalog.repository.CountryRepository;
import com.mediastep.beecow.catalog.service.CityService;
import com.mediastep.beecow.catalog.service.CountryService;
import com.mediastep.beecow.catalog.service.DistrictService;
import com.mediastep.beecow.catalog.service.WardService;
import com.mediastep.beecow.catalog.service.mapper.CityMapper;
import com.mediastep.beecow.catalog.service.mapper.CountryMapper;
import com.mediastep.beecow.catalog.service.mapper.CountryVMMapper;
import com.mediastep.beecow.catalog.service.mapper.CountryVMWithoutCitiesMapper;
import com.mediastep.beecow.catalog.web.rest.vm.CountryVM;
import com.mediastep.beecow.common.dto.CityDTO;
import com.mediastep.beecow.common.dto.CountryDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Country.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService{

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CountryMapper countryMapper;

    @Inject
    private CountryVMMapper countryVMMapper;

    @Inject
    private CountryVMWithoutCitiesMapper countryVMWithoutCitiesMapper;

    @Inject
    private CityService cityService;

    @Inject
    private DistrictService districtService;

    @Inject
    private WardService wardService;

    @Inject
    private CityMapper cityMapper;

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save
     * @return the persisted entity
     */
    public CountryDTO save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        Country country = countryMapper.countryDTOToCountry(countryDTO);
        country = countryRepository.save(country);
        CountryDTO result = countryMapper.countryToCountryDTO(country);
        return result;
    }

    /**
     *  Get all the countries.
     *
     *  @param pageable the pagination information
     *  @param withCities
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    @Cacheable(Constants.CacheName.CATALOG_COUNTRY_ALL)
    public Page<CountryVM> findAll(Pageable pageable, Boolean withCities) {
        log.debug("Request to get all Countries");
        Page<Country> result = countryRepository.findAll(pageable);
        if (withCities) {
            return result.map(country -> {
                TreeSet<City> sortedCity = country.getCities().stream().collect(Collectors.toCollection(() -> new TreeSet<City>(Comparator.comparing(City::getOutCountry))));
                country.setCities(sortedCity);

                CountryVM countryVM = countryVMMapper.countryToCountryDTO(country);

                // get district only VN
				if (Constants.COUNTRY_CODE_VN.equalsIgnoreCase(country.getCode())) {
					List<CityDTO> cityDTOs = cityMapper.citiesToCityDTOs(new ArrayList<>(country.getCities()));
					countryVM.setCities(cityDTOs);
				}
                return countryVM;
            });
        } else {
            return result.map(country -> countryVMWithoutCitiesMapper.countryToCountryDTO(country));
        }
    }

    /**
     *  Get one country by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    @Cacheable(Constants.CacheName.CATALOG_COUNTRY_BY_ID)
    public CountryVM findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        Country country = countryRepository.findById(id).orElse(null);
        CountryVM countryDTO = countryVMMapper.countryToCountryDTO(country);
        return countryDTO;
    }

    /**
     *  Get the "code" country.
     *
     *  @param code the code of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    @Cacheable(Constants.CacheName.CATALOG_COUNTRY_BY_CODE)
    public CountryVM findOneByCode(String code, Boolean withCities) {
        log.debug("Request to get Country : {}", code);
        Country country = countryRepository.findOneByCode(code);
        CountryVM countryDTO;
        if (withCities) {
            if(country!=null){
            TreeSet<City> sortedCity = country.getCities().stream().collect(Collectors.toCollection(() -> new TreeSet<City>(Comparator.comparing(City::getOutCountry))));
            country.setCities(sortedCity);}
            countryDTO = countryVMMapper.countryToCountryDTO(country);
        } else {
            countryDTO = countryVMWithoutCitiesMapper.countryToCountryDTO(country);
        }
        return countryDTO;
    }

    @Override
    public List<CountryVM> findAllByCodes(List<String> codes, Boolean withCities) {
        log.debug("Request to find all Countries by codes : {}", codes);

        if (CollectionUtils.isEmpty(codes)) {
            return Collections.EMPTY_LIST;
        }

        List<Country> countries = countryRepository.findAllByCodeIn(codes);
        List<CountryVM> countryDTOS = new ArrayList<>();

        if (!CollectionUtils.isEmpty(countries)) {
            if (withCities) {
                for (Country country : countries) {
                    TreeSet<City> sortedCity = country.getCities().stream().collect(Collectors.toCollection(() -> new TreeSet<City>(Comparator.comparing(City::getOutCountry))));
                    country.setCities(sortedCity);
                }
            }
            countryDTOS.addAll(countries.stream().map(countryVMWithoutCitiesMapper::countryToCountryDTO).distinct().collect(Collectors.toList()));
        }
        return countryDTOS;
    }

    /**
     *  Delete the  country by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CountryVM> getActiveCountries() {
        log.debug("Request to get all active Countries");

        List<CountryVM> result = countryRepository.findByIsShow(true).stream()
                .map(country -> countryVMMapper.countryToCountryDTO(country))
                .collect(Collectors.toCollection(LinkedList::new));
        result.sort(CountryDTO.CountryOrderComparator);

        for (CountryDTO country: result){
            country.getCities().removeIf(city -> !city.getIsShow());
        	country.getCities().sort(CityDTO.CityOrderComparator);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<CountryVM> getActiveCountriesByCode(String code) {
        log.debug("Request to get all active Countries by code");

        List<CountryVM> result = countryRepository.findByIsShowAndCode(true, code).stream()
            .map(country -> countryVMMapper.countryToCountryDTO(country))
            .collect(Collectors.toCollection(LinkedList::new));
        result.sort(CountryDTO.CountryOrderComparator);

        for (CountryDTO country: result){
            country.getCities().removeIf(city -> !city.getIsShow());
            country.getCities().sort(CityDTO.CityOrderComparator);
        }

        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable(Constants.CacheName.CATALOG_COUNTRY_WITH_CITIES_BY_CODE)
    public List<CountryVM> getCountriesCitiesByCode(String code) {
        log.debug("Request to get all cities by Countries code {}", code);

        // I don't know why he return a list while the country code is unique, but keep old behavior for the running system
        Country country = countryRepository.findOneByCode(code);
        if (country != null) {
            CountryVM countryDTO = countryVMMapper.countryToCountryDTO(country);
            countryDTO.getCities().sort(CityDTO.CityOrderComparator);
            return Lists.newArrayList(countryDTO);
        }

        return Collections.emptyList();
    }

	@Override
    @Cacheable(Constants.CacheName.CATALOG_COUNTRY_BY_ADDRESS)
    public String getAddressByCode(String cityCode, String districtCode, String wardCode) {

		String address = "";

		// ward
		if (!StringUtils.isEmpty(wardCode)) {
			Optional<WardNewDTO> ward = wardService.findOneByCode(wardCode);

			if (ward.isPresent()) {
				address += ward.get().getInCountry() + ", ";
			}
		}

		// district
		if (!StringUtils.isEmpty(districtCode)) {
			Optional<DistrictDTO> district = districtService.findOne(districtCode);

			if (district.isPresent()) {
				address += district.get().getInCountry() + ", ";
			}
		}

		// city
		if (!StringUtils.isEmpty(cityCode)) {
			CityDTO city = cityService.findOne(cityCode);

			if (city != null) {
				address += city.getInCountry();
			}
		}
		return address;
	}

    @Override
    public String findCurrencySymbolByCurrencyCode(String currencyCode) {
        return countryRepository.findCurrencySymbolByCurrencyCode(currencyCode);
    }
}
