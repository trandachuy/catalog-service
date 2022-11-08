package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.service.LanguageService;
import com.mediastep.beecow.catalog.service.dto.LanguageDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Language.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

    @Inject
    private LanguageService languageService;

    /**
     * POST  /languages : Create a new language.
     *
     * @param languageDTO the languageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new languageDTO, or with status 400 (Bad Request) if the language has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/languages")
    @Timed
    public ResponseEntity<LanguageDTO> createLanguage(@RequestBody LanguageDTO languageDTO) throws URISyntaxException {
        log.debug("REST request to save Language : {}", languageDTO);
        if (languageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "language", "idexists", "A new language cannot already have an ID")
            ).body(null);
        }
        LanguageDTO result = languageService.save(languageDTO);
        return ResponseEntity.created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, "language", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /languages : Updates an existing language.
     *
     * @param languageDTO the languageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated languageDTO,
     * or with status 400 (Bad Request) if the languageDTO is not valid,
     * or with status 500 (Internal Server Error) if the languageDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/languages")
    @Timed
    public ResponseEntity<LanguageDTO> updateLanguage(@RequestBody LanguageDTO languageDTO) throws URISyntaxException {
        log.debug("REST request to update Language : {}", languageDTO);
        if (languageDTO.getId() == null) {
            return createLanguage(languageDTO);
        }
        LanguageDTO result = languageService.save(languageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, "language", languageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /languages : get all the languages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of languages in body
     */
    @GetMapping("/languages")
    @Timed
    public List<LanguageDTO> getAllLanguages() {
        log.debug("REST request to get all Languages");
        return languageService.findAll();
    }

    /**
     * GET  /languages/:id : get the "id" language.
     *
     * @param id the id of the languageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the languageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/languages/{id}")
    @Timed
    public ResponseEntity<LanguageDTO> getLanguage(@PathVariable Long id) {
        log.debug("REST request to get Language : {}", id);
        LanguageDTO languageDTO = languageService.findOne(id);
        return Optional.ofNullable(languageDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /languages/:id : delete the "id" language.
     *
     * @param id the id of the languageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/languages/{id}")
    @Timed
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        log.debug("REST request to delete Language : {}", id);
        languageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, "language", id.toString())).build();
    }

}
