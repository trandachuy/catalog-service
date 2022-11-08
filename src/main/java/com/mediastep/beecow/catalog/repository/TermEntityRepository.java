/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.TermEntity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TermEntity entity.
 */
@SuppressWarnings("unused")
public interface TermEntityRepository extends JpaRepository<TermEntity,Long> {

}
