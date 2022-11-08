package com.mediastep.beecow.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Language entity.
 */
public class LanguageDTO implements Serializable {

    private Long id;

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

        LanguageDTO languageDTO = (LanguageDTO) o;

        if ( ! Objects.equals(id, languageDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LanguageDTO{" +
            "id=" + id +
            ", displayValue='" + displayValue + "'" +
            '}';
    }
}
