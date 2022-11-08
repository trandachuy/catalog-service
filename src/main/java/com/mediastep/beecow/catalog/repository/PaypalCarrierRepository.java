package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.PaypalCarrier;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PaypalCarrier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaypalCarrierRepository extends JpaRepository<PaypalCarrier, Long> {
}
