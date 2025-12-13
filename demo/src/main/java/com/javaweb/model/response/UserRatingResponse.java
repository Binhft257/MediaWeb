package com.javaweb.model.response;

import java.util.Date;

public class UserRatingResponse  {
    private Integer userRatingId;
    private Date createdAt;
    private Integer ratingValue;
    private Integer userId;
    private String userName;
    private String userAvatar;

    public Integer getUserRatingId() {
        return userRatingId;
    }

    public void setUserRatingId(Integer userRatingId) {
        this.userRatingId = userRatingId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}