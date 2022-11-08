/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 12/1/2017
 * Author: Loi Tran <email:loi.tran@mediastep.com>
 *
 */

package com.mediastep.beecow.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Taxonomy.
 */
@Entity
@Table(name = "taxonomy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Taxonomy extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @OneToMany(mappedBy = "taxonomy")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Term> terms = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Taxonomy name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Taxonomy displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Set<Term> getTerms() {
        return terms;
    }

    public Taxonomy terms(Set<Term> terms) {
        this.terms = terms;
        return this;
    }

    public Taxonomy addTerm(Term term) {
        terms.add(term);
        term.setTaxonomy(this);
        return this;
    }

    public Taxonomy removeTerm(Term term) {
        terms.remove(term);
        term.setTaxonomy(null);
        return this;
    }

    public void setTerms(Set<Term> terms) {
        this.terms = terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Taxonomy taxonomy = (Taxonomy) o;
        if (taxonomy.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, taxonomy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Taxonomy{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", displayName='" + displayName + "'" +
            '}';
    }
}
