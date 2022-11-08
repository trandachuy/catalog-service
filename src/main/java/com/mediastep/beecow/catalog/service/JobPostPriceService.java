package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.domain.JobPostPrice;
import com.mediastep.beecow.catalog.repository.JobPostPriceRepository;
import com.mediastep.beecow.catalog.service.dto.JobPostPriceDTO;
import com.mediastep.beecow.catalog.service.mapper.JobPostPriceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing JobPostPrice.
 */
@Service
@Transactional
public class JobPostPriceService {

    private final Logger log = LoggerFactory.getLogger(JobPostPriceService.class);

    private final JobPostPriceRepository jobPostPriceRepository;

    private final JobPostPriceMapper jobPostPriceMapper;

    public JobPostPriceService(JobPostPriceRepository jobPostPriceRepository, JobPostPriceMapper jobPostPriceMapper) {
        this.jobPostPriceRepository = jobPostPriceRepository;
        this.jobPostPriceMapper = jobPostPriceMapper;
    }

    /**
     * Save a jobPostPrice.
     *
     * @param jobPostPriceDTO the entity to save
     * @return the persisted entity
     */
    public JobPostPriceDTO save(JobPostPriceDTO jobPostPriceDTO) {
        log.debug("Request to save JobPostPrice : {}", jobPostPriceDTO);
        JobPostPrice jobPostPrice = jobPostPriceMapper.jobPostPriceDTOToJobPostPrice(jobPostPriceDTO);
        jobPostPrice = jobPostPriceRepository.save(jobPostPrice);
        JobPostPriceDTO result = jobPostPriceMapper.jobPostPriceToJobPostPriceDTO(jobPostPrice);
        return result;
    }

    /**
     *  Get all the jobPostPrices.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<JobPostPriceDTO> findAll() {
        log.debug("Request to get all JobPostPrices");
        List<JobPostPriceDTO> result = jobPostPriceRepository.findAll().stream()
            .map(jobPostPriceMapper::jobPostPriceToJobPostPriceDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one jobPostPrice by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public JobPostPriceDTO findOne(Long id) {
        log.debug("Request to get JobPostPrice : {}", id);
        JobPostPrice jobPostPrice = jobPostPriceRepository.findById(id).orElse(null);
        JobPostPriceDTO jobPostPriceDTO = jobPostPriceMapper.jobPostPriceToJobPostPriceDTO(jobPostPrice);
        return jobPostPriceDTO;
    }

    /**
     *  Delete the  jobPostPrice by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JobPostPrice : {}", id);
        jobPostPriceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<JobPostPriceDTO> getByCountryCode(String countryCode) {
        log.debug("Request to get all JobPostPrices by countryCode: {}", countryCode);
        List<JobPostPriceDTO> result = jobPostPriceRepository.findByCountryCode(countryCode).stream()
            .map(jobPostPriceMapper::jobPostPriceToJobPostPriceDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        result.sort(JobPostPriceDTO.numberOfCVComparator);
        return result;
    }
}
