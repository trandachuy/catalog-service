package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.Language;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Language entity.
 */
@SuppressWarnings("unused")
public interface LanguageRepository extends JpaRepository<Language,Long> {

}
