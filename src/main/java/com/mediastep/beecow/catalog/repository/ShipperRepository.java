package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.Shipper;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Shipper entity.
 */
@SuppressWarnings("unused")
public interface ShipperRepository extends JpaRepository<Shipper,Long> {
    List<Shipper> findAllByCountryId(Long id);
}
