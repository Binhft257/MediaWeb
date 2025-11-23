package com.javaweb.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "Video_Games")
public class VideoGamesEntity {

    @Id
    @Column(name = "media_item_id")
    private Integer mediaItemId;

    @OneToOne
    @JoinColumn(name = "media_item_id")
    @MapsId
    private MediaItemEntity mediaItem;

    @Column(name = "developer")
    private String developer;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "platform")
    private String platform;

    @Column(name = "min_requirement")
    private String minRequirement;

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

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getMinRequirement() {
        return minRequirement;
    }

    public void setMinRequirement(String minRequirement) {
        this.minRequirement = minRequirement;
    }
}
