package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.dto.PackageCurrencyDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mediastep.beecow.catalog.domain.PackageCurrency}.
 */
public interface PackageCurrencyService {

    /**
     * Save a packageCurrency.
     *
     * @param packageCurrencyDTO the entity to save.
     * @return the persisted entity.
     */
    PackageCurrencyDTO save(PackageCurrencyDTO packageCurrencyDTO);

    /**
     * Get all the packageCurrencies.
     *
     * @return the list of entities.
     */
    List<PackageCurrencyDTO> findAll();


    /**
     * Get the "id" packageCurrency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PackageCurrencyDTO> findOne(Long id);

    /**
     * Delete the "id" packageCurrency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    PackageCurrencyDTO getPackageCurrencyByCurrencyCode(String currencyCode);

    PackageCurrencyDTO getPackageCurrencyByLocale(String locale);
}
