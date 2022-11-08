package com.mediastep.beecow.catalog.service;

import com.mediastep.beecow.catalog.domain.Image;
import com.mediastep.beecow.catalog.repository.ImageRepository;
import com.mediastep.beecow.catalog.service.dto.ImageDTO;
import com.mediastep.beecow.catalog.service.mapper.ImageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Image.
 */
@Service
@Transactional
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    /**
     * Save a image.
     *
     * @param imageDTO the entity to save
     * @return the persisted entity
     */
    public ImageDTO save(ImageDTO imageDTO) {
        log.debug("Request to save Image : {}", imageDTO);
        Image image = imageMapper.imageDTOToImage(imageDTO);
        image = imageRepository.save(image);
        ImageDTO result = imageMapper.imageToImageDTO(image);
        return result;
    }

    /**
     *  Get all the images.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ImageDTO> findAll() {
        log.debug("Request to get all Images");
        List<ImageDTO> result = imageRepository.findAll().stream()
            .map(imageMapper::imageToImageDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one image by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ImageDTO findOne(Long id) {
        log.debug("Request to get Image : {}", id);
        Image image = imageRepository.findById(id).orElse(null);
        ImageDTO imageDTO = imageMapper.imageToImageDTO(image);
        return imageDTO;
    }

    /**
     *  Delete the  image by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Image : {}", id);
        imageRepository.deleteById(id);
    }
}
