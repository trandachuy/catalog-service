package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.catalog.service.dto.JobPostPriceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity JobPostPrice and its DTO JobPostPriceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobPostPriceMapper {

    JobPostPriceDTO jobPostPriceToJobPostPriceDTO(JobPostPrice jobPostPrice);

    List<JobPostPriceDTO> jobPostPricesToJobPostPriceDTOs(List<JobPostPrice> jobPostPrices);

    JobPostPrice jobPostPriceDTOToJobPostPrice(JobPostPriceDTO jobPostPriceDTO);

    List<JobPostPrice> jobPostPriceDTOsToJobPostPrices(List<JobPostPriceDTO> jobPostPriceDTOs);
}
