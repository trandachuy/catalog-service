package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.catalog.service.dto.ImageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Image and its DTO ImageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImageMapper {

    ImageDTO imageToImageDTO(Image image);

    List<ImageDTO> imagesToImageDTOs(List<Image> images);

    Image imageDTOToImage(ImageDTO imageDTO);

    List<Image> imageDTOsToImages(List<ImageDTO> imageDTOs);
}
