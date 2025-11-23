package com.javaweb.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "Music")
public class MusicEntity {

    @Id
    @Column(name = "media_item_id")
    private Integer mediaItemId;

    @OneToOne
    @JoinColumn(name = "media_item_id")
    @MapsId
    private MediaItemEntity mediaItem;

    @Column(name = "album")
    private String album;

    @Column(name = "artist")
    private String artist;

    @Column(name = "composer")
    private String composer;

    @Column(name = "track_number")
    private Integer trackNumber;

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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }
}
