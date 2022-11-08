package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.service.dto.WardValidationDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Ward native repository.
 */
@Repository
public interface WardNativeRepository {

    /**
     * Gets all ward of country.
     *
     * @param countryCode the country code
     * @return the all ward of country
     */
    List<WardValidationDTO> getAllWardOfCountry(String countryCode);

    List<WardValidationDTO> getAllWardByNameAndCountry(Long countryId, String keyword, String lang);

}
