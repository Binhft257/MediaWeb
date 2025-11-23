package com.javaweb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Movies")
public class MoviesEntity {

    @Id
    @Column(name = "media_item_id")
    private Integer mediaItemId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "media_item_id")
    private MediaItemEntity mediaItem;

    @Column(name = "producers")
    private String producers;

    @Column(name = "director")
    private String director;

    @Column(name = "cast")
    private String cast;

    @Column(name = "run_time_minutes")
    private Integer runTimeMinutes;

    @Column(name = "release_year")
    private Integer releaseYear;

    public Integer getMediaItemId() {
        return mediaItemId;
    }

    public void setMediaItemId(Integer mediaItemId) {
        this.mediaItemId = mediaItemId;
    }

    public MediaItemEntity getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItemEntity mediaItem) {
        this.mediaItem = mediaItem;
    }

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Integer getRunTimeMinutes() {
        return runTimeMinutes;
    }

    public void setRunTimeMinutes(Integer runTimeMinutes) {
        this.runTimeMinutes = runTimeMinutes;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}
