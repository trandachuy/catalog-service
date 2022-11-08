package com.mediastep.beecow.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Language extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_value")
    private String displayValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public Language displayValue(String displayValue) {
        this.displayValue = displayValue;
        return this;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Language language = (Language) o;
        if (language.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, language.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Language{" +
            "id=" + id +
            ", displayValue='" + displayValue + "'" +
            '}';
    }
}
