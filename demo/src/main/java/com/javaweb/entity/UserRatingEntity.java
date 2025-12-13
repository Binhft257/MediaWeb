package com.javaweb.entity;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "User_Rating")
public class UserRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_rating_id")
    private Integer userRatingId;

    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue;

    @Column(name = "rated_at", nullable = false)
    private Date ratedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_item_id", nullable = false)
    private MediaItemEntity mediaItem;

    public Integer getUserRatingId() {
        return userRatingId;
    }

    public void setUserRatingId(Integer userRatingId) {
        this.userRatingId = userRatingId;
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Date getRatedAt() {
        return ratedAt;
    }

    public void setRatedAt(Date ratedAt) {
        this.ratedAt = ratedAt;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public MediaItemEntity getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItemEntity mediaItem) {
        this.mediaItem = mediaItem;
    }
}