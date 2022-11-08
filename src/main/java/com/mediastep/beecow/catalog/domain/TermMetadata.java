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
 * A TermMetadata.
 */
@Entity
@Table(name = "term_metadata")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TermMetadata extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meta_key")
    private String metaKey;

    @Column(name = "meta_value")
    private String metaValue;

    @ManyToOne
    private Term term;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public TermMetadata metaKey(String metaKey) {
        this.metaKey = metaKey;
        return this;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public TermMetadata metaValue(String metaValue) {
        this.metaValue = metaValue;
        return this;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public Term getTerm() {
        return term;
    }

    public TermMetadata term(Term term) {
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
        TermMetadata termMetadata = (TermMetadata) o;
        if (termMetadata.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, termMetadata.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TermMetadata{" +
            "id=" + id +
            ", metaKey='" + metaKey + "'" +
            ", metaValue='" + metaValue + "'" +
            '}';
    }
}
