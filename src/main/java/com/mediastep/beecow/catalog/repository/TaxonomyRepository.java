/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.Taxonomy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Taxonomy entity.
 */
@SuppressWarnings("unused")
public interface TaxonomyRepository extends JpaRepository<Taxonomy,Long> {
    Taxonomy findOneByName(String name);
}
