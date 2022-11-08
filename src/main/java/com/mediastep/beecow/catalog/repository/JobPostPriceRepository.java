package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.JobPostPrice;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobPostPrice entity.
 */
@SuppressWarnings("unused")
public interface JobPostPriceRepository extends JpaRepository<JobPostPrice,Long> {
	
	List<JobPostPrice> findByCountryCode(String countryCode);

}
