package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.domain.Language;
import com.mediastep.beecow.catalog.repository.LanguageRepository;
import com.mediastep.beecow.catalog.service.dto.LanguageDTO;
import com.mediastep.beecow.catalog.service.mapper.LanguageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Language.
 */
@Service
@Transactional
public class LanguageService {

    private final Logger log = LoggerFactory.getLogger(LanguageService.class);

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private LanguageMapper languageMapper;

    /**
     * Save a language.
     *
     * @param languageDTO the entity to save
     * @return the persisted entity
     */
    public LanguageDTO save(LanguageDTO languageDTO) {
        log.debug("Request to save Language : {}", languageDTO);
        Language language = languageMapper.languageDTOToLanguage(languageDTO);
        language = languageRepository.save(language);
        LanguageDTO result = languageMapper.languageToLanguageDTO(language);
        return result;
    }

    /**
     *  Get all the languages.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> findAll() {
        log.debug("Request to get all Languages");
        List<LanguageDTO> result = languageRepository.findAll().stream()
            .map(languageMapper::languageToLanguageDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one language by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LanguageDTO findOne(Long id) {
        log.debug("Request to get Language : {}", id);
        Language language = languageRepository.findById(id).orElse(null);
        LanguageDTO languageDTO = languageMapper.languageToLanguageDTO(language);
        return languageDTO;
    }

    /**
     *  Delete the  language by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Language : {}", id);
        languageRepository.deleteById(id);
    }
}
