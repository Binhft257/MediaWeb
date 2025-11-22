package com.javaweb.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "Books")
public class BooksEntity {

    @Id
    @Column(name = "media_item_id")
    private Integer mediaItemId;

    @OneToOne
    @JoinColumn(name = "media_item_id")
    private MediaItemEntity mediaItem;

    @Column(name = "author")
    private String author;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "edition")
    private String edition;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }
}

