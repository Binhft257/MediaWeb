package com.javaweb.model.response;

import java.util.Date;

public class MediaUploadHistoryResponse {
    private Integer mediaItemId;
    private String title;
    private String mediaType;
    private Date createdAt;
    private String imagePath;
    private String description;

    public Integer getMediaItemId() { return mediaItemId; }
    public void setMediaItemId(Integer mediaItemId) { this.mediaItemId = mediaItemId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
