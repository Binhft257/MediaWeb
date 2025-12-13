package com.javaweb.model.request;

public abstract class TrackMediaRequest {
    private Integer mediaItemId;

    public Integer getMediaItemId() {
        return mediaItemId;
    }

    public void setMediaItemId(Integer mediaItemId) {
        this.mediaItemId = mediaItemId;
    }
}
