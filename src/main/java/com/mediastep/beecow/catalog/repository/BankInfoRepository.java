package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.BankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the BankInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankInfoRepository extends JpaRepository<BankInfo, Long> {

    Optional<BankInfo> findFirstById(Long id);

    void deleteById(Long id);
}
