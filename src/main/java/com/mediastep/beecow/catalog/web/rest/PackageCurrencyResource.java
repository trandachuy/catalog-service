package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.dto.PackageCurrencyDTO;
import com.mediastep.beecow.catalog.service.PackageCurrencyService;

import io.github.jhipster.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mediastep.beecow.catalog.domain.PackageCurrency}.
 */
@RestController
@RequestMapping("/api")
public class PackageCurrencyResource {

    private final Logger log = LoggerFactory.getLogger(PackageCurrencyResource.class);

    private static final String ENTITY_NAME = "catalogServicesPackageCurrency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackageCurrencyService packageCurrencyService;

    public PackageCurrencyResource(PackageCurrencyService packageCurrencyService) {
        this.packageCurrencyService = packageCurrencyService;
    }

    /**
     * {@code POST  /package-currencies} : Create a new packageCurrency.
     *
     * @param packageCurrencyDTO the packageCurrencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packageCurrencyDTO, or with status {@code 400 (Bad Request)} if the packageCurrency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PostMapping("/package-currencies")
//    @Timed
//    public ResponseEntity<PackageCurrencyDTO> createPackageCurrency(@RequestBody PackageCurrencyDTO packageCurrencyDTO) throws URISyntaxException {
//        log.debug("REST request to save PackageCurrency : {}", packageCurrencyDTO);
//        if (packageCurrencyDTO.getId() != null) {
//            throw new BadRequestAlertException("A new packageCurrency cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        PackageCurrencyDTO result = packageCurrencyService.save(packageCurrencyDTO);
//        return ResponseEntity.created(new URI("/api/package-currencies/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

    /**
     * {@code PUT  /package-currencies} : Updates an existing packageCurrency.
     *
     * @param packageCurrencyDTO the packageCurrencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageCurrencyDTO,
     * or with status {@code 400 (Bad Request)} if the packageCurrencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packageCurrencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PutMapping("/package-currencies")
//    @Timed
//    public ResponseEntity<PackageCurrencyDTO> updatePackageCurrency(@RequestBody PackageCurrencyDTO packageCurrencyDTO) throws URISyntaxException {
//        log.debug("REST request to update PackageCurrency : {}", packageCurrencyDTO);
//        if (packageCurrencyDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        PackageCurrencyDTO result = packageCurrencyService.save(packageCurrencyDTO);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packageCurrencyDTO.getId().toString()))
//            .body(result);
//    }

    /**
     * {@code GET  /package-currencies} : get all the packageCurrencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packageCurrencies in body.
     */
    @GetMapping("/package-currencies")
    @Timed
    public List<PackageCurrencyDTO> getAllPackageCurrencies() {
        log.debug("REST request to get all PackageCurrencies");
        return packageCurrencyService.findAll();
    }

    /**
     * {@code GET  /package-currencies/:id} : get the "id" packageCurrency.
     *
     * @param id the id of the packageCurrencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packageCurrencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/package-currencies/{id}")
    @Timed
    public ResponseEntity<PackageCurrencyDTO> getPackageCurrency(@PathVariable Long id) {
        log.debug("REST request to get PackageCurrency : {}", id);
        Optional<PackageCurrencyDTO> packageCurrencyDTO = packageCurrencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packageCurrencyDTO);
    }

//    /**
//     * {@code DELETE  /package-currencies/:id} : delete the "id" packageCurrency.
//     *
//     * @param id the id of the packageCurrencyDTO to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/package-currencies/{id}")
//    @Timed
//    public ResponseEntity<Void> deletePackageCurrency(@PathVariable Long id) {
//        log.debug("REST request to delete PackageCurrency : {}", id);
//        packageCurrencyService.delete(id);
//        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
//    }

    /**
     * {@code GET  /package-currencies/currency-code/{currencyCode}} : get the packageCurrency by currencyCode.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the packageCurrency in body.
     */
    @GetMapping("/package-currencies/currency-code/{currencyCode}")
    @Timed
    public PackageCurrencyDTO getPackageCurrencyByCurrencyCode(@PathVariable String currencyCode) {
        log.debug("REST request to get PackageCurrency by currencyCode");
        return packageCurrencyService.getPackageCurrencyByCurrencyCode(currencyCode);
    }

    /**
     * {@code GET  /package-currencies/locale/{locale}} : get the packageCurrency by locale.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the packageCurrency in body.
     */
    @GetMapping("/package-currencies/locale/{locale}")
    @Timed
    public PackageCurrencyDTO getPackageCurrencyByLocale(@PathVariable String locale) {
        log.debug("REST request to get PackageCurrency by locale");
        return packageCurrencyService.getPackageCurrencyByLocale(locale);
    }
}
