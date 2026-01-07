package com.javaweb.model.response;

import java.util.Date;

public class UserHistoryResponse {
    private Integer mediaItemId;
    private String title;
    private String typeName;
    private String urlItem;
    private Date createdAt;

    public Integer getMediaItemId() { return mediaItemId; }
    public void setMediaItemId(Integer mediaItemId) { this.mediaItemId = mediaItemId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getUrlItem() { return urlItem; }
    public void setUrlItem(String urlItem) { this.urlItem = urlItem; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
