package com.javaweb.model.response;

import java.util.Date;

public class TrackingResponse {
    private Integer logId;
    private String status;
    private String comment;
    private Integer rating;
    private Date createdAt;

    private MediaSearchResponse media;

    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public MediaSearchResponse getMedia() { return media; }
    public void setMedia(MediaSearchResponse media) { this.media = media; }
}
