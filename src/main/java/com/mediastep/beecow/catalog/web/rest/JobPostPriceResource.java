package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import com.mediastep.beecow.catalog.service.JobPostPriceService;
import com.mediastep.beecow.catalog.service.dto.JobPostPriceDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JobPostPrice.
 */
@RestController
@RequestMapping("/api")
public class JobPostPriceResource {

    private final Logger log = LoggerFactory.getLogger(JobPostPriceResource.class);

    private static final String ENTITY_NAME = "jobPostPrice";

    private final JobPostPriceService jobPostPriceService;

    public JobPostPriceResource(JobPostPriceService jobPostPriceService) {
        this.jobPostPriceService = jobPostPriceService;
    }

    /**
     * POST  /job-post-prices : Create a new jobPostPrice.
     *
     * @param jobPostPriceDTO the jobPostPriceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobPostPriceDTO, or with status 400 (Bad Request) if the jobPostPrice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-post-prices")
    @Timed
    public ResponseEntity<JobPostPriceDTO> createJobPostPrice(@RequestBody JobPostPriceDTO jobPostPriceDTO) throws URISyntaxException {
        log.debug("REST request to save JobPostPrice : {}", jobPostPriceDTO);
        if (jobPostPriceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, "idexists", "A new jobPostPrice cannot already have an ID")
            ).body(null);
        }
        JobPostPriceDTO result = jobPostPriceService.save(jobPostPriceDTO);
        return ResponseEntity.created(new URI("/api/job-post-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-post-prices : Updates an existing jobPostPrice.
     *
     * @param jobPostPriceDTO the jobPostPriceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobPostPriceDTO,
     * or with status 400 (Bad Request) if the jobPostPriceDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobPostPriceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-post-prices")
    @Timed
    public ResponseEntity<JobPostPriceDTO> updateJobPostPrice(@RequestBody JobPostPriceDTO jobPostPriceDTO) throws URISyntaxException {
        log.debug("REST request to update JobPostPrice : {}", jobPostPriceDTO);
        if (jobPostPriceDTO.getId() == null) {
            return createJobPostPrice(jobPostPriceDTO);
        }
        JobPostPriceDTO result = jobPostPriceService.save(jobPostPriceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, jobPostPriceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-post-prices : get all the jobPostPrices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobPostPrices in body
     */
    @GetMapping("/job-post-prices")
    @Timed
    public List<JobPostPriceDTO> getAllJobPostPrices() {
        log.debug("REST request to get all JobPostPrices");
        return jobPostPriceService.findAll();
    }

    /**
     * GET  /job-post-prices/:id : get the "id" jobPostPrice.
     *
     * @param id the id of the jobPostPriceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobPostPriceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/job-post-prices/{id}")
    @Timed
    public ResponseEntity<JobPostPriceDTO> getJobPostPrice(@PathVariable Long id) {
        log.debug("REST request to get JobPostPrice : {}", id);
        JobPostPriceDTO jobPostPriceDTO = jobPostPriceService.findOne(id);
        return Optional.ofNullable(jobPostPriceDTO)
        .map(result -> new ResponseEntity<>(
            result,
            HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /job-post-prices/:id : delete the "id" jobPostPrice.
     *
     * @param id the id of the jobPostPriceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-post-prices/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobPostPrice(@PathVariable Long id) {
        log.debug("REST request to delete JobPostPrice : {}", id);
        jobPostPriceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(CatalogServicesApp.class.getSimpleName(), false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/job-post-prices/getByCountryCode")
    @Timed
    public List<JobPostPriceDTO> getByCountryCode(String countryCode) {
        log.debug("REST request to get all JobPostPrices by countryCode: {}", countryCode);
        return jobPostPriceService.getByCountryCode(countryCode);
    }

}
