package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.City;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the City entity.
 */
@SuppressWarnings("unused")
public interface CityRepository extends JpaRepository<City,Long>,JpaSpecificationExecutor {
    List<City> findAllByCountryId(Long id);

    List<City> findAllByCodeIn(List<String> codes);
    City findOneByCode(String code);

    List<City> findAllByIdIn(Set<Long> ids);
}
