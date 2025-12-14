package com.javaweb.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Media_Genre")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Integer genreId;

    @Column(name = "genre_name", nullable = false, unique = true)
    private String genreName;

    @Column(name = "description")
    private String description;

    // Many-to-Many with MediaItem
    @ManyToMany(mappedBy = "genres")
    private List<MediaItemEntity> mediaItems;

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MediaItemEntity> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MediaItemEntity> mediaItems) {
        this.mediaItems = mediaItems;
    }
}