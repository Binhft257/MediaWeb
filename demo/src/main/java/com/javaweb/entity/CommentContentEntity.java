package com.javaweb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Comment_Content_Entity")
public class CommentContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private UserCommentEntity comment;

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserCommentEntity getComment() {
        return comment;
    }

    public void setCommentId(UserCommentEntity comment) {
        this.comment = comment;
    }
}