/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 9/11/2018
 * Author: Dai Mai <email: dai.mai@mediastep.com>
 */

package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Spring Data  repository for the District entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistrictRepository extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<District> findById(Long id);

    /**
     * Delete by id.
     *
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * Find by city id list.
     *
     * @param cityId the city id
     * @return the list
     */
    List<District> findByCity_Id(Long cityId);

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<District> findByCode(String code);

    List<District> findByCodeIn(List<String> codes);

    List<District> findByCity_Code(String cityCode);

    @Query(value = "SELECT * FROM district d " +
            "LEFT JOIN city ci ON ci.id = d.city_id " +
            "LEFT JOIN country co ON co.id = ci.country_id " +
            "WHERE co.code = :countryCode", nativeQuery = true)
    List<District> findByCountryCode(@Param("countryCode") String countryCode);

    List<District> findAllByIdIn(Set<Long> ids);
}
