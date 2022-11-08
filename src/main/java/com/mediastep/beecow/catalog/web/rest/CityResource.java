package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.dto.*;
import com.mediastep.beecow.catalog.request.AddressRequest;
import com.mediastep.beecow.catalog.response.AddressResponse;
import com.mediastep.beecow.catalog.response.AddressValidator;
import com.mediastep.beecow.catalog.service.CityService;
import com.mediastep.beecow.catalog.service.CountryService;
import com.mediastep.beecow.catalog.service.DistrictService;
import com.mediastep.beecow.catalog.service.WardService;
import com.mediastep.beecow.catalog.service.mapper.CountryMapper;
import com.mediastep.beecow.catalog.web.rest.vm.CountryVM;
import com.mediastep.beecow.common.aop.annotation.PublishedOpenApi;
import com.mediastep.beecow.common.dto.CityDTO;
import com.mediastep.beecow.common.dto.CountryDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing City.
 */
@RestController
@RequestMapping("/api")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    @Inject
    private CityService cityService;

    @Inject
    private CountryService countryService;

    @Inject
    private DistrictService districtService;

    @Inject
    private WardService wardService;

    @Inject
    private CountryMapper countryMapper;

    /**
     * POST  /cities : Create a new city.
     *
     * @param cityDTO the cityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cityDTO, or with status 400 (Bad Request) if the city has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cities")
    @Timed
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) throws URISyntaxException {
        log.debug("REST request to save City : {}", cityDTO);
        if (cityDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "city", "idexists", "A new city cannot already have an ID")
            ).body(null);
        }
        CityDTO result = cityService.save(cityDTO);
        return ResponseEntity.created(new URI("/api/cities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "city", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cities : Updates an existing city.
     *
     * @param cityDTO the cityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cityDTO,
     * or with status 400 (Bad Request) if the cityDTO is not valid,
     * or with status 500 (Internal Server Error) if the cityDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cities")
    @Timed
    public ResponseEntity<CityDTO> updateCity(@RequestBody CityDTO cityDTO) throws URISyntaxException {
        log.debug("REST request to update City : {}", cityDTO);
        if (cityDTO.getId() == null) {
            return createCity(cityDTO);
        }
        CityDTO result = cityService.save(cityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "city", cityDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cities : get all the cities.
     *
     * @return the List cities with status 200 (OK) and the list of cities in body
     */
    @GetMapping("/cities")
    @Timed
    public List<CityDTO> getAllCities() {
        log.debug("REST request to get a page of Cities");

        return cityService.findAll();
    }

    /**
     * GET  /cities/:id : get the "id" city.
     *
     * @param id the id of the cityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cityDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cities/{id}")
    @Timed
    public ResponseEntity<CityDTO> getCity(@PathVariable Long id) {
        log.debug("REST request to get City : {}", id);
        CityDTO cityDTO = cityService.findOne(id);
        return Optional.ofNullable(cityDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cities/:id : delete the "id" city.
     *
     * @param id the id of the cityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cities/{id}")
    @Timed
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        log.debug("REST request to delete City : {}", id);
        cityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "city", id.toString())).build();
    }

    /**
     * GET  /cities : get all the cities.
     *
     * @param code
     * @return the ResponseEntity with status 200 (OK) and the list of cities in body
     */
    @ApiOperation("Get cities by country code")
    @GetMapping("/country/{code}/cities")
    @Timed
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_BY_COUNTRY_CODE)
    @PublishedOpenApi
    public List<CitySimpleDTO> getAllCities(@ApiParam("Country's code") @PathVariable String code) {
        log.debug("REST request to get a page of Cities");
        List<CitySimpleDTO> cities = new ArrayList<>();
        CountryDTO countryDTO = this.countryService.findOneByCode(code, false);
        if(countryDTO != null) {
            cities = cityService.findAll(countryDTO.getId());
        }

        Collections.sort(cities, CitySimpleDTO::compareTo);

        return cities;
    }

    /**
     * GET  /cities : get all the cities.
     *
     * @param countryCode
     * @param keyword
     * @return the ResponseEntity with status 200 (OK) and the list of cities in body
     */
    @ApiOperation("search cities by country code and name")
    @GetMapping("/country/{countryCode}/cities/{keyword}")
    @Timed
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_BY_COUNTRY_CODE_AND_KEYWORD)
    public List<CitySimpleDTO> getCityByCountryAndName(@ApiParam("country code") @PathVariable String countryCode, @ApiParam("keyword") @PathVariable String keyword) {
        log.debug("REST search to get a page of Cities");
        List<CitySimpleDTO> cities = new ArrayList<>();
        if(this.countryService.findOneByCode(countryCode, true) != null) {
            cities = cityService.searchCity(this.countryService.findOneByCode(countryCode, true).getCode(), keyword);
        }

        Collections.sort(cities, CitySimpleDTO::compareTo);

        return cities;
    }

    @ApiOperation("search cities by country code and name")
    @GetMapping("/country/{countryCode}/cities/code/{code}")
    @Timed
//    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_BY_COUNTRY_CODE_AND_CODE)
    public List<CityDTO> getCityByCountryAndCode(@ApiParam("country code") @PathVariable String countryCode, @ApiParam("city code") @PathVariable String code) {
        List<CityDTO> cities = new ArrayList<>();
        CountryVM countryVM = this.countryService.findOneByCode(countryCode, true);
        if(countryVM != null) {
            cities = cityService.searchCityByCountryAndCodeOrName(new HashSet<>(Collections.singletonList(countryVM.getId())), code);
        }
        return cities;
    }

    /**
     * GET  /cities/:code : get the "code" city.
     *
     * @param code the code of the CitySimpleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the CitySimpleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/city/{code}")
    @Timed
    public ResponseEntity<CitySimpleDTO> getCityByCode(@PathVariable String code) {
        log.debug("REST request to get City : {}", code);
        CitySimpleDTO cityDTO = cityService.searchCityByCode(code);
        return Optional.ofNullable(cityDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/country/{countryCode}/cities/tree")
    @Timed
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_CITY_TREE_BY_COUNTRY_CODE)
    public List<CityTreeDTO> getAllCitiesTree(@ApiParam("country code") @PathVariable String countryCode) {
        log.debug("REST request to get all Cities tree of country {}", countryCode);

        List<CitySimpleDTO> cities = new ArrayList<>();
        CountryVM countryVM = this.countryService.findOneByCode(countryCode, true);

        if (countryVM != null) {
            cities = cityService.findAll(countryVM.getId());
        }

        List<CityTreeDTO> results = cities.parallelStream().map(CityTreeDTO::fromCitySimpleDTO).collect(Collectors.toList());

        results.parallelStream().forEach(cityTreeDTO -> {
            List<DistrictDTO> districtDTO = this.districtService.findAll(cityTreeDTO.getCode());
            List<DistrictTreeDTO> districtTreeDTOs = districtDTO.stream().map(DistrictTreeDTO::fromDistrictDTO).collect(Collectors.toList());

            districtTreeDTOs.parallelStream().forEach(districtTreeDTO -> {
                List<WardNewDTO> wardNewDTOs = this.wardService.findAllByDistrict(districtTreeDTO.getCode());
                districtTreeDTO.setWards(wardNewDTOs);
            });

            cityTreeDTO.setDistricts(districtTreeDTOs);
        });

        results.sort(CityTreeDTO::compareTo);
        return results;
    }

    @PostMapping("/countries/address")
    @Timed
    public List<AddressResponse> getAddresses(@ApiParam("address request") @RequestBody List<AddressRequest> requests) {
        log.debug("REST request to get addresses {}", requests);

        List<String> countryCodes = requests.parallelStream().map(AddressRequest::getCountryCode).collect(Collectors.toList());
        List<CountryVM> countryVMS = this.countryService.findAllByCodes(countryCodes, false);
        List<String> cityCodes = requests.parallelStream().map(AddressRequest::getCityCode).collect(Collectors.toList());
        List<CitySimpleDTO> cities = this.cityService.findSimpleAllByCodes(cityCodes);
        List<String> districtCodes = requests.parallelStream().map(AddressRequest::getDistrictCode).collect(Collectors.toList());
        List<DistrictDTO> districts = this.districtService.findAllByCodes(districtCodes);
        List<String> wardCodes = requests.parallelStream().map(AddressRequest::getWardCode).collect(Collectors.toList());
        List<WardNewDTO> wards = this.wardService.findAllByCodes(wardCodes);

        List<AddressResponse> results = requests.parallelStream().map(request -> {
            AddressResponse.AddressResponseBuilder addressResponseBuilder = AddressResponse.builder();

            countryVMS.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getCountryCode())).findFirst().ifPresent(c -> {
                addressResponseBuilder.country(countryMapper.countryDTOToCountrySimpleDTO(c));
            });
            cities.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getCityCode())).findFirst().ifPresent(c -> {
                addressResponseBuilder.city(c);
            });

            if ("VN".equalsIgnoreCase(request.getCountryCode())) {
                districts.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getDistrictCode())).findFirst().ifPresent(d -> {
                    addressResponseBuilder.district(d);
                });
                wards.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getWardCode())).findFirst().ifPresent(w -> {
                    addressResponseBuilder.ward(w);
                });
            }

            return addressResponseBuilder.build();
        }).collect(Collectors.toList());

        return results;
    }

    @PostMapping("/countries/address/validate")
    @Timed
    public AddressValidator validateAddresses(@ApiParam("address request") @RequestBody List<AddressRequest> requests) throws Exception {
        log.debug("REST request to validate addresses {}", requests);

        List<String> countryCodes = requests.parallelStream().map(AddressRequest::getCountryCode).collect(Collectors.toList());
        List<CountryVM> countryVMS = this.countryService.findAllByCodes(countryCodes, false);
        List<String> cityCodes = requests.parallelStream().map(AddressRequest::getCityCode).collect(Collectors.toList());
        List<CityDTO> cities = this.cityService.findAllByCodes(cityCodes);
        List<String> districtCodes = requests.parallelStream().map(AddressRequest::getDistrictCode).collect(Collectors.toList());
        List<DistrictDTO> districts = this.districtService.findAllByCodes(districtCodes);
        List<String> wardCodes = requests.parallelStream().map(AddressRequest::getWardCode).collect(Collectors.toList());
        List<WardNewDTO> wards = this.wardService.findAllByCodes(wardCodes);

		for (AddressRequest request : requests) {
			Optional<CountryVM> countryVMOpt = countryVMS.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getCountryCode())).findFirst();

			if (!countryVMOpt.isPresent()) {
				return AddressValidator.builder()
						.defaultMessage("Country code not found")
						.entityName("country")
						.errorKey("country.notFound")
						.errorCode(HttpStatus.NOT_FOUND.value())
						.build();
			}

			Optional<CityDTO> cityDTOOpt = cities.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getCityCode())).findFirst();

			if (!cityDTOOpt.isPresent()) {
				return AddressValidator.builder()
						.defaultMessage("City code not found")
						.entityName("city")
						.errorKey("city.notFound")
						.errorCode(HttpStatus.NOT_FOUND.value())
						.build();
			}

			if (!Objects.equals(cityDTOOpt.get().getCountryId(), countryVMOpt.get().getId())) {
				return AddressValidator.builder()
						.defaultMessage("City not belong to country")
						.entityName("city")
						.errorKey("city.notBelong")
						.errorCode(HttpStatus.BAD_REQUEST.value())
						.build();
			}

			if ("VN".equalsIgnoreCase(request.getCountryCode())) {
				//Inside country
				Optional<DistrictDTO> districtDTOOpt = districts.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getDistrictCode())).findFirst();

				if (!districtDTOOpt.isPresent()) {
					return AddressValidator.builder()
							.defaultMessage("District code not found")
							.entityName("district")
							.errorKey("district.notFound")
							.errorCode(HttpStatus.NOT_FOUND.value())
							.build();
				}

				if (!Objects.equals(districtDTOOpt.get().getCityId(), cityDTOOpt.get().getId())) {
					return AddressValidator.builder()
							.defaultMessage("District not belong to city")
							.entityName("district")
							.errorKey("district.notBelong")
							.errorCode(HttpStatus.BAD_REQUEST.value())
							.build();
				}

				Optional<WardNewDTO> wardNewDTOOpt = wards.parallelStream().filter(c -> Objects.equals(c.getCode(), request.getWardCode())).findFirst();

				if (!wardNewDTOOpt.isPresent()) {
					return AddressValidator.builder()
							.defaultMessage("Ward code not found")
							.entityName("ward")
							.errorKey("ward.notFound")
							.errorCode(HttpStatus.NOT_FOUND.value())
							.build();
				}

				if (!Objects.equals(wardNewDTOOpt.get().getDistrictId(), districtDTOOpt.get().getId())) {
					return AddressValidator.builder()
							.defaultMessage("Ward not belong to district")
							.entityName("ward")
							.errorKey("ward.notBelong")
							.errorCode(HttpStatus.BAD_REQUEST.value())
							.build();
				}
			}
		}

        return null;
    }

    @GetMapping("/get-detail-city-district-ward")
    @Timed
    public CityDistrictWardResponse getCityDistrictWardByCode(@RequestParam(name = "districtCode", required = false) String districtCode,
                                                              @RequestParam(name = "wardCode", required = false) String wardCode) {
        if (StringUtils.isAllBlank(districtCode, wardCode)){
            return new CityDistrictWardResponse();
        }
        CityDistrictWardResponse res = cityService.getCityDistrictWardByCode(districtCode, wardCode);
        return res;
    }
}
