package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.service.CountryService;
import com.mediastep.beecow.catalog.service.ShipperService;
import com.mediastep.beecow.catalog.service.dto.ShipperDTO;
import com.mediastep.beecow.catalog.service.dto.SimpleShipperDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Shipper.
 */
@RestController
@RequestMapping("/api")
public class ShipperResource {

    private final Logger log = LoggerFactory.getLogger(ShipperResource.class);

    @Inject
    private ShipperService shipperService;

    @Inject
    private CountryService countryService;

    /**
     * POST  /shippers : Create a new shipper.
     *
     * @param shipperDTO the shipperDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shipperDTO, or with status 400 (Bad Request) if the shipper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shippers")
    @Timed
    public ResponseEntity<ShipperDTO> createShipper(@RequestBody ShipperDTO shipperDTO) throws URISyntaxException {
        log.debug("REST request to save Shipper : {}", shipperDTO);
        if (shipperDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "shipper", "idexists", "A new shipper cannot already have an ID")
            ).body(null);
        }
        ShipperDTO result = shipperService.save(shipperDTO);
        return ResponseEntity.created(new URI("/api/shippers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "shipper", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shippers : Updates an existing shipper.
     *
     * @param shipperDTO the shipperDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shipperDTO,
     * or with status 400 (Bad Request) if the shipperDTO is not valid,
     * or with status 500 (Internal Server Error) if the shipperDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shippers")
    @Timed
    public ResponseEntity<ShipperDTO> updateShipper(@RequestBody ShipperDTO shipperDTO) throws URISyntaxException {
        log.debug("REST request to update Shipper : {}", shipperDTO);
        if (shipperDTO.getId() == null) {
            return createShipper(shipperDTO);
        }
        ShipperDTO result = shipperService.save(shipperDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "shipper", shipperDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shippers : get all the shippers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of shippers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/shippers")
    @Timed
    public ResponseEntity<List<ShipperDTO>> getAllShippers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Shippers");
        Page<ShipperDTO> page = shipperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shippers : get all the shippers.
     *
     * @param code  the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of shippers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @ApiOperation("get list shippers by country")
    @GetMapping("/country/{code}/shippers")
    @Timed
    public List<SimpleShipperDTO> getAllShippers(@ApiParam("country id") @PathVariable("code") String code)
        throws URISyntaxException {
        log.debug("REST request to get a page of Shippers");
        List<SimpleShipperDTO> shippers = new ArrayList<>();
        if(this.countryService.findOneByCode(code, true) != null) {
            shippers = shipperService.findAll(countryService.findOneByCode(code, true).getId());
        }

        return shippers;
    }

    /**
     * GET  /shippers/:id : get the "id" shipper.
     *
     * @param id the id of the shipperDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shipperDTO, or with status 404 (Not Found)
     */
    @GetMapping("/shippers/{id}")
    @Timed
    public ResponseEntity<ShipperDTO> getShipper(@PathVariable Long id) {
        log.debug("REST request to get Shipper : {}", id);
        ShipperDTO shipperDTO = shipperService.findOne(id);
        return Optional.ofNullable(shipperDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shippers/:id : delete the "id" shipper.
     *
     * @param id the id of the shipperDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shippers/{id}")
    @Timed
    public ResponseEntity<Void> deleteShipper(@PathVariable Long id) {
        log.debug("REST request to delete Shipper : {}", id);
        shipperService.delete(id);
        return ResponseEntity.ok().headers(
            HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "shipper", id.toString())
        ).build();
    }


}
