package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.service.CountryService;
import com.mediastep.beecow.catalog.web.rest.vm.CountryVM;
import com.mediastep.beecow.common.aop.annotation.PublishedOpenApi;
import com.mediastep.beecow.common.dto.CountryDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api")
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    @Inject
    private CountryService countryService;

    /**
     * POST  /countries : Create a new country.
     *
     * @param countryDTO the countryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new countryDTO, or with status 400 (Bad Request) if the country has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/countries")
    @Timed
    public ResponseEntity<CountryDTO> createCountry(@RequestBody CountryDTO countryDTO) throws URISyntaxException {
        log.debug("REST request to save Country : {}", countryDTO);
        if (countryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "country", "idexists", "A new country cannot already have an ID")
            ).body(null);
        }
        CountryDTO result = countryService.save(countryDTO);
        return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "country", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /countries : Updates an existing country.
     *
     * @param countryDTO the countryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated countryDTO,
     * or with status 400 (Bad Request) if the countryDTO is not valid,
     * or with status 500 (Internal Server Error) if the countryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/countries")
    @Timed
    public ResponseEntity<CountryDTO> updateCountry(@RequestBody CountryDTO countryDTO) throws URISyntaxException {
        log.debug("REST request to update Country : {}", countryDTO);
        if (countryDTO.getId() == null) {
            return createCountry(countryDTO);
        }
        CountryDTO result = countryService.save(countryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "country", countryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /countries : get all the countries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of countries in body
     */
    @GetMapping("/countries")
    @Timed
    @HystrixCommand(
            commandKey = "max60"
    )
    @ApiOperation("Get all countries")
    @PublishedOpenApi
    public ResponseEntity<List<CountryVM>> getAllCountries(
        @ApiIgnore @RequestParam(value = "withCities", required = false, defaultValue = "false") Boolean withCities,
        @PageableDefault(
            sort = "outCountry",
            size = 300,
            direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug("REST request to get a page of Countries");
        Page<CountryVM> page = countryService.findAll(pageable, withCities);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/countries/code/{code}")
    @Timed
    public ResponseEntity<CountryDTO> getCountryByCode(@PathVariable String code,
                                                       @RequestParam(value = "withCities", required = false, defaultValue = "true") Boolean withCities) {
        log.debug("REST request to get Country by: {}", code);
        CountryDTO countryDTO = countryService.findOneByCode(code, withCities);
        return Optional.ofNullable(countryDTO)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/countries/get-symbol-by-currency-code/{currencyCode}")
    @Timed
    public ResponseEntity<String> getSymbolBycurrencyCode(@PathVariable String currencyCode ) {
        log.debug("REST request to get getSymbolBycurrencyCode by: {}", currencyCode);
        String currencySymbol = countryService.findCurrencySymbolByCurrencyCode(currencyCode);
        return Optional.ofNullable(currencySymbol)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /countries/:id : get the "id" country.
     *
     * @param id the id of the countryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the countryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/countries/{id}")
    @Timed
    public ResponseEntity<CountryDTO> getCountry(@PathVariable Long id) {
        log.debug("REST request to get Country : {}", id);
        CountryDTO countryDTO = countryService.findOne(id);
        return Optional.ofNullable(countryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /countries/:id : delete the "id" country.
     *
     * @param id the id of the countryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/countries/{id}")
    @Timed
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        log.debug("REST request to delete Country : {}", id);
        countryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "country", id.toString())).build();
    }

    @GetMapping("/countries/getActiveCountries")
    @Timed
    public ResponseEntity<List<CountryVM>> getActiveCountries() throws URISyntaxException {
        log.debug("REST request to get active Countries");
        List<CountryVM> result = countryService.getActiveCountries();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/countries/{code}/getActiveCountries")
    @Timed
    public ResponseEntity<List<CountryVM>> getActiveCountries(@PathVariable("code") String code) throws URISyntaxException {
        log.debug("REST request to get active Countries by code");
        List<CountryVM> result = countryService.getActiveCountriesByCode(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/countries/{code}/getCountryCities")
    @Timed
    public ResponseEntity<List<CountryVM>> getCitiesListByCountryCode(@PathVariable("code") String code) throws URISyntaxException {
        log.debug("REST request to get active Countries by code");
        List<CountryVM> result = countryService.getCountriesCitiesByCode(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

	@GetMapping("/countries/get-address-name")
	@Timed
	public ResponseEntity<String> getCountriesCitiesByCode(
			@RequestParam(name = "cityCode", required = false) String cityCode,
			@RequestParam(name = "districtCode", required = false) String districtCode,
			@RequestParam(name = "wardCode", required = false) String wardCode
	) throws URISyntaxException {

		log.debug("get-address-name with city {}, district {}, ward {}", cityCode, districtCode, wardCode);

		String address = countryService.getAddressByCode(cityCode, districtCode, wardCode);
		return new ResponseEntity<>(address, HttpStatus.OK);
	}
}
