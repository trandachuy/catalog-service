package com.mediastep.beecow.catalog.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Image entity.
 */
public class ImageDTO implements Serializable {

    private Long id;

    private Long imageId;

    private String urlPrefix;

    private String emoji;

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageDTO imageDTO = (ImageDTO) o;

        if ( ! Objects.equals(id, imageDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
            "id=" + id +
            ", imageId='" + imageId + "'" +
            ", urlPrefix='" + urlPrefix + "'" +
            '}';
    }
}
