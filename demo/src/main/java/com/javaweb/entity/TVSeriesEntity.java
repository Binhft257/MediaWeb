package com.javaweb.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "TV_Series")
public class TVSeriesEntity {

    @Id
    @Column(name = "media_item_id")
    private Integer mediaItemId;

    @OneToOne
    @JoinColumn(name = "media_item_id")
    private MediaItemEntity mediaItem;

    @Column(name = "creator")
    private String creator;

    @Column(name = "total_seasons")
    private Integer totalSeasons;

    @Column(name = "total_episodes")
    private Integer totalEpisodes;

    @Column(name = "production_company")
    private String productionCompany;

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Integer getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(Integer totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }
}

