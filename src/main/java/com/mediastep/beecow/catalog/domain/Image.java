package com.mediastep.beecow.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Image.
 */
@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "url_prefix")
    private String urlPrefix;
    
    @Column(name = "emoji")
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

    public Image imageId(Long imageId) {
        this.imageId = imageId;
        return this;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public Image urlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
        return this;
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
        Image image = (Image) o;
        if (image.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", imageId='" + imageId + "'" +
            ", urlPrefix='" + urlPrefix + "'" +
            '}';
    }
}
