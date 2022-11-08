package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.catalog.service.dto.LanguageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Language and its DTO LanguageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LanguageMapper {

    LanguageDTO languageToLanguageDTO(Language language);

    List<LanguageDTO> languagesToLanguageDTOs(List<Language> languages);

    Language languageDTOToLanguage(LanguageDTO languageDTO);

    List<Language> languageDTOsToLanguages(List<LanguageDTO> languageDTOs);
}
