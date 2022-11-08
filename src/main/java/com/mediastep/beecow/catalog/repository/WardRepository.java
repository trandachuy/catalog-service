package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.District;
import com.mediastep.beecow.catalog.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Ward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WardRepository extends JpaRepository<Ward, Long> , JpaSpecificationExecutor<Ward> {

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<Ward> findById(Long id);

    /**
     * Delete by id.
     *
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * Find all by district code list.
     *
     * @param districtCode the district code
     * @return the list
     */
    List<Ward> findAllByDistrict_CodeOrderByInCountryAsc(String districtCode);

    /**
     * Find by code optional.
     *
     * @param wardCode the ward code
     * @return the optional
     */
    Optional<Ward> findByCode(String wardCode);

    @Query(value = "SELECT * FROM ward w " +
            "LEFT JOIN district d ON d.id = w.district_id " +
            "LEFT JOIN city ci ON ci.id = d.city_id " +
            "LEFT JOIN country co ON co.id = ci.country_id " +
            "WHERE co.code = :countryCode", nativeQuery = true)
    List<Ward> findByCountryCode(@Param("countryCode") String countryCode);

	List<Ward> findByCodeIn(List<String> codes);
}
