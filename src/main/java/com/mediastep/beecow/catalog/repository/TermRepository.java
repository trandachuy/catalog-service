/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.domain.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Term entity.
 */
public interface TermRepository extends JpaRepository<Term,Long> {
    List<Term> findAllByParent_Id(Long parentId, Sort orderBy);
    List<Term> findAllByLastModifiedDateBetween(ZonedDateTime fromDate, ZonedDateTime toDate, Sort orderBy);
    List<Term> findAllByTaxonomy_NameAndLastModifiedDateBetween(String taxonomyName, ZonedDateTime fromDate, ZonedDateTime toDate, Sort orderBy);
    List<Term> findAllByTermLevel(Integer level, Sort orderBy);

    @Query("select term "
            + "from Term term "
            + "left outer join Taxonomy taxonomy on term.taxonomy.id=taxonomy.id "
            + "where "
                + "((:taxonomy is null) or (taxonomy.name = :taxonomy)) "
                + "and "
                + "((:fromDate is null) or (term.lastModifiedDate between :fromDate and :toDate)) "
                + "and "
                + "((:metaKey is null) or (term.id in (select metadata.term.id from TermMetadata metadata where metadata.metaKey = :metaKey and (:metaValue is null or metadata.metaValue = :metaValue)))) "
    )
    List<Term> findAll(@Param("taxonomy") String taxonomyName, @Param("metaKey") String metaKey, @Param("metaValue") String metaValue, @Param("fromDate") ZonedDateTime fromDate, @Param("toDate") ZonedDateTime toDate, Sort orderBy);

    @Query("select term "
            + "from Term term "
            + "left outer join Taxonomy taxonomy on term.taxonomy.id=taxonomy.id "
            + "where "
                + "((:level is null) or (term.termLevel = :level)) "
                + "and "
                + "((:taxonomy is null) or (taxonomy.name = :taxonomy)) "
                + "and "
                + "((:metaKey is null) or (term.id in (select metadata.term.id from TermMetadata metadata where metadata.metaKey = :metaKey and (:metaValue is null or metadata.metaValue = :metaValue)))) "
    )
    List<Term> findAll(@Param("level") Integer level, @Param("taxonomy") String taxonomyName, @Param("metaKey") String metaKey, @Param("metaValue") String metaValue, Sort orderBy);

    @Query("select term "
            + "from Term term "
            + "left outer join Taxonomy taxonomy on term.taxonomy.id=taxonomy.id "
            + "left outer join Term term2 on term.parent.id=term2.id "
            + "where "
                + "(((:ids) is null) or (term.id in (:ids))) "
                + "and "
                + "((:taxonomy is null) or (taxonomy.name = :taxonomy)) "
                + "and "
                + "((:parentId is null) or (term2.id = :parentId)) "
                + "and "
                + "((:level is null) or (term.termLevel = :level)) "
                + "and "
                + "((:metaKey is null) or (term.id in (select metadata.term.id from TermMetadata metadata where metadata.metaKey = :metaKey and (:metaValue is null or metadata.metaValue = :metaValue)))) "
    )
    List<Term> findAll(@Param("ids") List<Long> ids, @Param("taxonomy") String taxonomy, @Param("parentId") Long parentId, @Param("level") Integer level, @Param("metaKey") String metaKey, @Param("metaValue") String metaValue, Sort orderBy);

//    select * from postgres."catalog-services".term tblv12
//    where tblv12.parent_id in (
//        select tbterm.id
//        from postgres."catalog-services".term tbterm
//        left join postgres."catalog-services".taxonomy tbtax on tbterm.taxonomy_id = tbtax.id
//        where tbtax."name" = 'product-catalog'
//        and tbterm.id in (1005, 1006, 1008)
//    or tbterm.parent_id in (1005, 1006, 1008) )
//    or tblv12.id in (1005, 1006, 1008)


    @Query("select tbfinal "
        + "from Term tbfinal "
        + "left outer join Taxonomy taxonomy on tbfinal.taxonomy.id=taxonomy.id "
        + "left outer join Term term2 on tbfinal.parent.id=term2.id "
        + "where tbfinal.id in (:ids) "
        + "and "
        + "((:taxonomy is null) or (taxonomy.name = :taxonomy)) "
        + "and "
        + "((:metaKey is null) or (tbfinal.id in (select metadata.term.id from TermMetadata metadata where metadata.metaKey = :metaKey and (:metaValue is null or metadata.metaValue = :metaValue)))) "
    )
    List<Term> findAllByIdList(@Param("ids") List<Long> ids, @Param("taxonomy") String taxonomy, @Param("metaKey") String metaKey, @Param("metaValue") String metaValue, Sort orderBy);


    Term findOneByIdAndTaxonomy_Name(Long id, String taxonomyName);

    Page<Term> findByParentIdIn(List<Long> parentIds, Pageable pageable);

    Page<Term> findByParentIdInAndIdIn(List<Long> parentIds, List<Long> ids, Pageable pageable);
}
