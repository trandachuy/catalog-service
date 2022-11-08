/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TermEntity.
 */
@Entity
@Table(name = "term_entity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Deprecated
public class TermEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_uri")
    private String entityUri;

    @ManyToOne
    private Term term;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityUri() {
        return entityUri;
    }

    public TermEntity entityUri(String entityUri) {
        this.entityUri = entityUri;
        return this;
    }

    public void setEntityUri(String entityUri) {
        this.entityUri = entityUri;
    }

    public Term getTerm() {
        return term;
    }

    public TermEntity term(Term term) {
        this.term = term;
        return this;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TermEntity termEntity = (TermEntity) o;
        if (termEntity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, termEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TermEntity{" +
            "id=" + id +
            ", entityUri='" + entityUri + "'" +
            '}';
    }
}
