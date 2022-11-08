package com.mediastep.beecow.catalog.repository;

import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.service.dto.DistrictValidationDTO;
import com.mediastep.beecow.catalog.service.dto.WardValidationDTO;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

/**
 * The type Ward native repository.
 */
@Repository
public class WardNativeRepositoryImpl implements WardNativeRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<WardValidationDTO> getAllWardOfCountry(String countryCode) {
        List<WardValidationDTO> result;
        SessionFactory sessionFactory = em.getEntityManagerFactory().unwrap(SessionFactory.class);
        StatelessSession session = sessionFactory.openStatelessSession();
        try {
            TypedQuery<WardValidationDTO> query = session
                    .createQuery("SELECT new" +
                                         " com.mediastep.beecow.catalog.service.dto.WardValidationDTO(w.id, w.code," +
                                         " w.inCountry, w.outCountry, d.id, d.code) " +
                                         " FROM Ward w " +
                                         " LEFT JOIN District d ON d.id = w.district.id\n" +
                                         "            LEFT JOIN City ci ON ci.id = d.city.id\n" +
                                         "            LEFT JOIN Country co ON co.id = ci.country.id\n" +
                                         "            WHERE co.code = :countryCode",
                                 WardValidationDTO.class);
            query.setParameter("countryCode", countryCode);
            result = query.getResultList();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.CacheName.CATALOG_GET_WARD_BY_NAME_AND_COUNTRY)
    public List<WardValidationDTO> getAllWardByNameAndCountry(Long countryId, String keyword, String lang) {
        List<WardValidationDTO> result;
        SessionFactory sessionFactory = em.getEntityManagerFactory().unwrap(SessionFactory.class);
        StatelessSession session = sessionFactory.openStatelessSession();
        try {
            TypedQuery<WardValidationDTO> query = lang.equalsIgnoreCase("vi")
                ? session.createQuery("SELECT new" +
                    " com.mediastep.beecow.catalog.service.dto.WardValidationDTO(w.id, w.code," +
                    " w.inCountry, w.outCountry, d.id, d.code) " +
                    " FROM Ward w " +
                    " LEFT JOIN District d ON d.id = w.district.id\n" +
                    "            LEFT JOIN City ci ON ci.id = d.city.id\n" +
                    "            LEFT JOIN Country co ON co.id = ci.country.id\n" +
                    "            WHERE co.id = :countryId and unaccent_vietnamese(lower(w.inCountry)) like '%' || lower(unaccent_vietnamese(:keyword)) || '%'"
                , WardValidationDTO.class)
                :  session.createQuery("SELECT new" +
                    " com.mediastep.beecow.catalog.service.dto.WardValidationDTO(w.id, w.code," +
                    " w.inCountry, w.outCountry, d.id, d.code) " +
                    " FROM Ward w " +
                    " LEFT JOIN District d ON d.id = w.district.id\n" +
                    "            LEFT JOIN City ci ON ci.id = d.city.id\n" +
                    "            LEFT JOIN Country co ON co.id = ci.country.id\n" +
                    "            WHERE co.id = :countryId and unaccent_vietnamese(lower(w.outCountry)) like '%' || lower(unaccent_vietnamese(:keyword)) || '%'"
                , WardValidationDTO.class);
            query.setParameter("countryId", countryId);
            query.setParameter("keyword", keyword);
            result = query.getResultList();
        } finally {
            session.close();
        }
        return result;
    }
}
