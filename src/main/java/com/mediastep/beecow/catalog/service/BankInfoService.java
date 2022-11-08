package com.mediastep.beecow.catalog.service;


import com.mediastep.beecow.catalog.dto.BankInfoDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mediastep.beecow.catalog.domain.BankInfo}.
 */
public interface BankInfoService {

    /**
     * Get all the bankInfos.
     *
     * @return the list of entities.
     */
    List<BankInfoDTO> findAll();

    /**
     * Get all the bankInfos.
     *
     * @return the list of entities.
     */
    Map<String, String> findAllBank();

    /**
     * Get the "id" bankInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankInfoDTO> findOne(Long id);

}
