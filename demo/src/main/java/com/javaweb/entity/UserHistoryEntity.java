package com.javaweb.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "UserHistory",
       uniqueConstraints = @UniqueConstraint(name = "uk_user_media", columnNames = {"user_id", "media_item_id"}))
public class UserHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_item_id", nullable = false)
    private MediaItemEntity mediaItem;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public Integer getHistoryId() { return historyId; }
    public void setHistoryId(Integer historyId) { this.historyId = historyId; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public MediaItemEntity getMediaItem() { return mediaItem; }
    public void setMediaItem(MediaItemEntity mediaItem) { this.mediaItem = mediaItem; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
