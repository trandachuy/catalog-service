/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 10/5/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.repository.specification;

import com.mediastep.beecow.catalog.domain.City;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CitySpecs {

    private static void buildMatchKeyword(Root<City> root, CriteriaBuilder builder, Predicate predicate, String keyword){
        if(!ObjectUtils.isEmpty(keyword)){
            StringBuilder stringBuilder = new StringBuilder("%").append(keyword).append("%");
            Predicate searchKeyword = builder.or(builder.like(builder.lower(root.get("outCountry")), stringBuilder.toString().toLowerCase()), builder.like(builder.lower(root.get("inCountry")), stringBuilder.toString().toLowerCase()));

            predicate.getExpressions().add(searchKeyword);
        }
    }

    private static void buildMatchKeywordByCode(Root<City> root, CriteriaBuilder builder, Predicate predicate, String keyword){
        if(!ObjectUtils.isEmpty(keyword)){
            Predicate searchKeyword = builder.and(builder.like(builder.lower(root.get("code")), ("%" + keyword + "%").toLowerCase()));
            predicate.getExpressions().add(searchKeyword);
        }
    }

    public static Specification<City> searchCity(String countryCode, String keyword) {
        return (root, query, builder) -> {
            Predicate byCountryCode = builder.equal(root.get("country").get("code"), countryCode);
            Predicate predicate = builder.and(byCountryCode);
            buildMatchKeyword(root, builder, predicate, keyword);

            return predicate;
        };
    }

    public static Specification<City> searchCityByCountryAndCode(Long countryId, String code) {
        return (root, query, builder) -> {
            Predicate byCountryCode = builder.equal(root.get("country").get("id"), countryId);
            Predicate predicate = builder.and(byCountryCode);
            buildMatchKeywordByCode(root, builder, predicate, code);
            return predicate;
        };
    }
}
