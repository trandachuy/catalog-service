package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.PackageCurrency;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PackageCurrency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageCurrencyRepository extends JpaRepository<PackageCurrency, Long> {
	PackageCurrency findByCurrencyCode(String currencyCode);
}
