package com.mediastep.beecow.catalog.repository.specification;

import com.mediastep.beecow.catalog.domain.Country_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;

@Repository
public class AddressSpecs<T> {

    public static final String UNACCENT_VIETNAMESE_FUNC = "unaccent_vietnamese";
    public static final String LIKE_CHAR = "%";

    private Predicate searchLikeOutCountry(String searchKeyword, Root<T> root, CriteriaBuilder builder) {
        searchKeyword = StringUtils.isEmpty(searchKeyword) ? StringUtils.EMPTY : searchKeyword.toLowerCase();
        Expression<String> unaccentOutCountry = builder.function(UNACCENT_VIETNAMESE_FUNC, String.class, builder.lower(root.get(Country_.OUT_COUNTRY)));
        Expression<String> unaccentOutCountryLike = builder.function(UNACCENT_VIETNAMESE_FUNC, String.class, builder.literal(LIKE_CHAR + searchKeyword + LIKE_CHAR));
        return builder.like(unaccentOutCountry, unaccentOutCountryLike);
    }

    private Predicate searchLikeInCountry(String searchKeyword, Root<T> root, CriteriaBuilder builder) {
        searchKeyword = StringUtils.isEmpty(searchKeyword) ? StringUtils.EMPTY : searchKeyword.toLowerCase();
        Expression<String> unaccentInCountry = builder.function(UNACCENT_VIETNAMESE_FUNC, String.class, builder.lower(root.get(Country_.IN_COUNTRY)));
        Expression<String> unaccentInCountryLike = builder.function(UNACCENT_VIETNAMESE_FUNC, String.class, builder.literal(LIKE_CHAR + searchKeyword + LIKE_CHAR));
        return builder.like(unaccentInCountry, unaccentInCountryLike);
    }

    private Predicate searchLikeCode(String searchKeyword, Root<T> root, CriteriaBuilder builder) {
        searchKeyword = StringUtils.isEmpty(searchKeyword) ? StringUtils.EMPTY : searchKeyword.toLowerCase();
        Expression<String> unaccentCode = builder.function(UNACCENT_VIETNAMESE_FUNC, String.class, builder.lower(root.get(Country_.CODE)));
        Expression<String> unaccentCodeLike = builder.function(UNACCENT_VIETNAMESE_FUNC, String.class, builder.literal(LIKE_CHAR + searchKeyword + LIKE_CHAR));
        return builder.like(unaccentCode, unaccentCodeLike);
    }

    public Specification<T> searchAllByParentIdsAndName(Set<Long> parentIds, String parentPath, String name) {
        return (root, query, builder) -> {
            Expression<String> parentExpression = root.get(parentPath);
            Predicate byCountry = parentExpression.in(parentIds);
            Predicate predicate = builder.and(byCountry);
            Predicate search = builder.and(
                this.searchLikeInCountry(name, root, builder),
                this.searchLikeOutCountry(name, root, builder)
            );
            predicate.getExpressions().add(search);
            return predicate;
        };
    }

    public Specification<T> searchAllByParentIdsAndCode(Set<Long> parentIds, String parentPath, String code) {
        return (root, query, builder) -> {
            Expression<String> parentExpression = root.get(parentPath);
            Predicate byCountry = parentExpression.in(parentIds);
            Predicate predicate = builder.and(byCountry);
            Predicate search = builder.and(
                this.searchLikeCode(code, root, builder)
            );
            predicate.getExpressions().add(search);
            return predicate;
        };
    }

    public Specification<T> searchAllByParentIdsAndCodeOrName(Set<Long> parentIds, String parentPath, String keyword) {
        return (root, query, builder) -> {
            Expression<String> parentExpression = root.get(parentPath);
            Predicate byParent = parentExpression.in(parentIds);
            Predicate predicate = builder.and(byParent);
            Predicate search = builder.or(
                this.searchLikeCode(keyword, root, builder),
                this.searchLikeOutCountry(keyword, root, builder),
                this.searchLikeInCountry(keyword, root, builder)
            );
            predicate.getExpressions().add(search);
            return predicate;
        };
    }

    public Specification<T> searchAllByCodeOrName(String keyword) {
        return (root, query, builder) -> {
            return builder.or(
                this.searchLikeCode(keyword, root, builder),
                this.searchLikeOutCountry(keyword, root, builder),
                this.searchLikeInCountry(keyword, root, builder)
            );
        };
    }

}
