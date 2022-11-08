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
 * A Term.
 */
@Entity
@Table(name = "term")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Term extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "slug")
    private String slug;

    @Column(name = "seo_description")
    private String seoDescription;

    @Column(name = "seo_keyword")
    private String seoKeyword;

    @Column(name = "term_level")
    private Integer termLevel;

    @Column(name = "term_order")
    private Integer termOrder;

    // TODO parallelStream() issue: when there are many streams, LAZY loading fails
    @OneToMany(mappedBy = "term", fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    // TODO #performance only load "basic-metadata"
    // @Filter(name="basicMetadata", condition=":metaKey LIKE %")
    private Set<TermMetadata> metadata = new HashSet<>();

    @ManyToOne
    private Taxonomy taxonomy;

    @ManyToOne
    private Term parent;

    public Term id(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Term name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Term displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getTermLevel() {
        return termLevel;
    }

    public Term termLevel(Integer termLevel) {
        this.termLevel = termLevel;
        return this;
    }

    public void setTermLevel(Integer termLevel) {
        this.termLevel = termLevel;
    }

    public Integer getTermOrder() {
        return termOrder;
    }

    public Term termOrder(Integer termOrder) {
        this.termOrder = termOrder;
        return this;
    }

    public void setTermOrder(Integer termOrder) {
        this.termOrder = termOrder;
    }
    public Set<TermMetadata> getMetadata() {
        return metadata;
    }

    public Term metadata(Set<TermMetadata> termMetadata) {
        this.metadata = termMetadata;
        return this;
    }

    public Term addMetadata(TermMetadata termMetadata) {
        metadata.add(termMetadata);
        termMetadata.setTerm(this);
        return this;
    }

    public Term removeMetadata(TermMetadata termMetadata) {
        metadata.remove(termMetadata);
        termMetadata.setTerm(null);
        return this;
    }

    public void setMetadata(Set<TermMetadata> termMetadata) {
        this.metadata = termMetadata;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public Term taxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
        return this;
    }

    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }

    public Term getParent() {
        return parent;
    }

    public Term parent(Term term) {
        this.parent = term;
        return this;
    }

    public void setParent(Term term) {
        this.parent = term;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Term term = (Term) o;
        if (term.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, term.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Term{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", displayName='" + displayName + '\'' +
            ", slug='" + slug + '\'' +
            ", seoDescription='" + seoDescription + '\'' +
            ", seoKeyword='" + seoKeyword + '\'' +
            ", termLevel=" + termLevel +
            ", termOrder=" + termOrder +
            ", metadata=" + metadata +
            ", taxonomy=" + taxonomy +
            ", parent=" + parent +
            '}';
    }
}
