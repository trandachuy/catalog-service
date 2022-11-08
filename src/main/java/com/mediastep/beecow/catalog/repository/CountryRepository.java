package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.Country;

import com.mediastep.beecow.catalog.domain.District;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
public interface CountryRepository extends JpaRepository<Country,Long> {
    Country findOneByCode(String code);

    List<Country> findAllByCodeIn(List<String> code);

    List<Country> findByIsShow(Boolean isShow);

    List<Country> findByIsShowAndCode(Boolean isShow, String code);


    @Query(value = "SELECT currency_symbol FROM country  WHERE currency_code = :currencyCode LIMIT 1", nativeQuery = true)
     String findCurrencySymbolByCurrencyCode(@Param("currencyCode") String currencyCode);
}
