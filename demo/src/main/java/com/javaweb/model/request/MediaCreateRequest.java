package com.javaweb.model.request;
import java.util.Date;
import java.util.List;

public abstract class MediaCreateRequest {
    private String typeName;
    private String title;
    private String description;
    private String language;
    private String country;
    private String contentRating;
    private Date releaseDate;
    private String urlItem;
    private List<String> genres;

    // Either a BooksRequest, a MoviesRequest, a MusicRequest, a TVSeriesRequest or a VideoGamesRequest
    private Object details;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        if (typeName != "Book" && typeName != "Music" && typeName != "Movie" && typeName != "TV Series" && typeName != "Video Game") {
            throw new IllegalArgumentException("Media item must be of type Book, Music, Movie, TV Series or Video Game.");
        }
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Media item must have a title.");
        }
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        if (!(details instanceof BooksRequest) && !(details instanceof MoviesRequest) && !(details instanceof MusicRequest) && !(details instanceof TVSeriesRequest) && !(details instanceof VideoGamesRequest)) {
            throw new IllegalArgumentException("Details must be a Books, Movies, Music, TVServies or a Video Games request.");
        }
    }
}
