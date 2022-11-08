package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface District native repository.
 */
@Repository
public interface DistrictNativeRepository {

    /**
     * Gets all district of country.
     *
     * @param countryCode the country code
     * @return the all district of country
     */
    List<DistrictValidationDTO> getAllDistrictOfCountry(String countryCode);

    List<DistrictValidationDTO> getAllDistrictByNameAndCountry(Long countryId, String keyword, String lang);
}
