/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.domain.util;

import java.util.HashMap;
import java.util.Map;

import com.mediastep.beecow.catalog.domain.AbstractAuditingEntity;
import com.mediastep.beecow.catalog.domain.Taxonomy;
import com.mediastep.beecow.catalog.domain.Term;
import com.mediastep.beecow.catalog.domain.TermMetadata;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
public final class EntityUtil {

    private static String MAP_ENTITY_SEP = ",";

    private static String KEY_VALUE_SEP = ":";

    /**
     * Clear all auditing information from entity
     * @param entity
     */
    public static <T extends AbstractAuditingEntity> T clearAuditingInfo(T entity) {
        entity.setCreatedBy(null);
        entity.setCreatedDate(null);
        entity.setLastModifiedBy(null);
        entity.setLastModifiedDate(null);
        return entity;
    }

    /**
     * Create sample object
     * @param id term ID
     * @return
     */
    private static <T extends AbstractAuditingEntity> T createSample(Class<T> clazz) {
        try {
            T sample = clazz.newInstance();
            return clearAuditingInfo(sample); // #important clean up auditing information from sample-object to construct WHERE cause
        } catch (InstantiationException | IllegalAccessException exc) {
            throw new RuntimeException("Failed to create sample for '" + clazz.getName() + "'", exc);
        }
    }

    /**
     * Convert string with below format to map.
     * <pre>
     * key1:value1,key2:value2,key3:value3
     * </pre>
     * @param mapAsString
     * @return
     */
    public static Map<String, String> stringToMap(String mapAsString) {
        if (mapAsString == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String[] entities = mapAsString.split(MAP_ENTITY_SEP);
        for (String entity : entities) {
            String[] keyValue = entity.split(KEY_VALUE_SEP);
            if (keyValue.length >= 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    /**
     * Create sample taxonomy
     * @param taxonomyName
     * @return sample taxonomy, null if taxonomyName == null
     */
    public static Taxonomy createSampleTaxonomy(String taxonomyName) {
        if (taxonomyName == null) {
            return null;
        }
        Taxonomy sample = createSample(Taxonomy.class);
        sample.setName(taxonomyName);
        return sample;
    }

    /**
     * Create sample term
     * @param id term ID
     * @return
     */
    public static Term createSampleTerm(Long id) {
        if (id == null) {
            return null;
        }
        Term sample = createSample(Term.class);
        sample.setId(id);
        return sample;
    }

    /**
     * Create sample term
     * @param taxonomyName
     * @param parentId
     * @param level
     * @param metadataList
     * @return
     */
    public static Term createSampleTerm(String taxonomyName, Long parentId, Integer level, Map<String, String> metadataList) {
        Term sample = createSample(Term.class);
        // Set taxonomy
        if (taxonomyName != null) {
            Taxonomy taxonomy = createSampleTaxonomy(taxonomyName);
            sample.setTaxonomy(taxonomy);
        }
        // Set parent
        if (parentId != null) {
            Term parent = createSampleTerm(parentId);
            sample.setParent(parent);
        }
        // Set properties
        if (level != null) {
            sample.setTermLevel(level);
        }
        if (metadataList != null && !metadataList.isEmpty()) {
            for (Map.Entry<String, String> metadataEntry : metadataList.entrySet()) {
                TermMetadata metadata = new TermMetadata();
                metadata.setMetaKey(metadataEntry.getKey());
                metadata.setMetaValue(metadataEntry.getValue());
                sample.getMetadata().add(metadata);
            }
        }
        return sample;
    }
}
