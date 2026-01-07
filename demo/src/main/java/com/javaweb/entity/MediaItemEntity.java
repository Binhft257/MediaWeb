package com.javaweb.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Media_Item")
public class MediaItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_item_id")
    private Integer mediaItemId;

    @ManyToOne
    @JoinColumn(name = "media_type_id")
    private MediaTypeEntity mediaType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "uploaded_by", nullable = false)
    private Integer uploadedBy;

    @Column(name = "description")
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "country")
    private String country;

    @Column(name = "content_rating")
    private String contentRating;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "url_item")
    private String urlItem;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Media_Genre",
            joinColumns = @JoinColumn(name = "media_item_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres;

    @OneToOne(mappedBy = "mediaItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MoviesEntity movie;

    @OneToOne(mappedBy = "mediaItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MusicEntity music;

    @OneToOne(mappedBy = "mediaItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BooksEntity book;

    @OneToOne(mappedBy = "mediaItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VideoGamesEntity videoGame;

    @OneToOne(mappedBy = "mediaItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TVSeriesEntity tvSeries;

    @OneToMany(mappedBy = "mediaItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserCommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "mediaItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserRatingEntity> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "mediaItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserMediaLogEntity> mediaLogs = new ArrayList<>();

    @OneToMany(mappedBy = "mediaItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserHistoryEntity> histories = new ArrayList<>();

    public List<UserCommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<UserCommentEntity> comments) {
        this.comments = comments;
    }

    public List<UserRatingEntity> getRatings() {
        return ratings;
    }

    public void setRatings(List<UserRatingEntity> ratings) {
        this.ratings = ratings;
    }

    public List<UserMediaLogEntity> getMediaLogs() {
        return mediaLogs;
    }

    public void setMediaLogs(List<UserMediaLogEntity> mediaLogs) {
        this.mediaLogs = mediaLogs;
    }

    public MoviesEntity getMovie() {
        return movie;
    }

    public void setMovie(MoviesEntity movie) {
        this.movie = movie;
    }

    public MusicEntity getMusic() {
        return music;
    }

    public void setMusic(MusicEntity music) {
        this.music = music;
    }

    public BooksEntity getBook() {
        return book;
    }

    public void setBook(BooksEntity book) {
        this.book = book;
    }

    public VideoGamesEntity getVideoGame() {
        return videoGame;
    }

    public void setVideoGame(VideoGamesEntity videoGame) {
        this.videoGame = videoGame;
    }

    public TVSeriesEntity getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(TVSeriesEntity tvSeries) {
        this.tvSeries = tvSeries;
    }

    public Integer getMediaItemId() {
        return mediaItemId;
    }

    public void setMediaItemId(Integer mediaItemId) {
        this.mediaItemId = mediaItemId;
    }

    public MediaTypeEntity getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaTypeEntity mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUrlItem() {
        return urlItem;
    }

    public void setUrlItem(String urlItem) {
        this.urlItem = urlItem;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUploadedBy() {
        return uploadedBy;
    }
    
    public void setUploadedBy(Integer uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public List<GenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntity> genres) {
        this.genres = genres;
    }
}