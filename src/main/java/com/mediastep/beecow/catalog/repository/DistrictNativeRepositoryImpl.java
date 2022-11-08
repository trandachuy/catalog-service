package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * The type District native repository.
 */
@Repository
public class DistrictNativeRepositoryImpl implements DistrictNativeRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<DistrictValidationDTO> getAllDistrictOfCountry(String countryCode) {
        List<DistrictValidationDTO> result;
        SessionFactory sessionFactory = em.getEntityManagerFactory().unwrap(SessionFactory.class);
        StatelessSession session = sessionFactory.openStatelessSession();
        try {
            TypedQuery<DistrictValidationDTO> query = session
                    .createQuery("SELECT new" +
                                         " com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO(d.id, d.code," +
                                         " d.inCountry, d.outCountry, d.zone, ci.id, ci.code) " +
                                         "FROM District d\n" +
                                         "            LEFT JOIN City ci ON ci.id = d.city.id \n" +
                                         "            LEFT JOIN Country co ON co.id = ci.country.id \n" +
                                         "            WHERE co.code = :countryCode",
                                 DistrictValidationDTO.class);
            query.setParameter("countryCode", countryCode);
            result = query.getResultList();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_GET_DISTRICT_BY_NAME_AND_COUNTRY)
    public List<DistrictValidationDTO> getAllDistrictByNameAndCountry(Long countryId, String keyword, String lang) {
        List<DistrictValidationDTO> result;
        SessionFactory sessionFactory = em.getEntityManagerFactory().unwrap(SessionFactory.class);
        StatelessSession session = sessionFactory.openStatelessSession();
        try {
            TypedQuery<DistrictValidationDTO> query = lang.equalsIgnoreCase("vi")
                ? session.createQuery("SELECT new" +
                    " com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO(d.id, d.code," +
                    " d.inCountry, d.outCountry, d.zone, ci.id, ci.code) " +
                    "FROM District d\n" +
                    "            LEFT JOIN City ci ON ci.id = d.city.id \n" +
                    "            LEFT JOIN Country co ON co.id = ci.country.id \n" +
                    "            WHERE co.id = :countryId and unaccent_vietnamese(lower(d.inCountry)) like '%' || lower(unaccent_vietnamese(:keyword)) || '%'"
                    , DistrictValidationDTO.class)
                :  session.createQuery("SELECT new" +
                    " com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO(d.id, d.code," +
                    " d.inCountry, d.outCountry, d.zone, ci.id, ci.code) " +
                    "FROM District d\n" +
                    "            LEFT JOIN City ci ON ci.id = d.city.id \n" +
                    "            LEFT JOIN Country co ON co.id = ci.country.id \n" +
                    "            WHERE co.id = :countryId and unaccent_vietnamese(lower(d.outCountry)) like '%' || lower(unaccent_vietnamese(:keyword)) || '%'"
                , DistrictValidationDTO.class);
            query.setParameter("countryId", countryId);
            query.setParameter("keyword", keyword);
            result = query.getResultList();
        } finally {
            session.close();
        }
        return result;
    }

}
