package com.javaweb.model.request;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "typeName",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MoviesRequest.class, name = "Movie"),
    @JsonSubTypes.Type(value = BooksRequest.class, name = "Book"),
    @JsonSubTypes.Type(value = MusicRequest.class, name = "Music"),
    @JsonSubTypes.Type(value = TVSeriesRequest.class, name = "TV Series"),
    @JsonSubTypes.Type(value = VideoGamesRequest.class, name = "Video Game")
})
public class MediaCreateRequest {
    private String typeName;
    private String title;
    private String description;
    private String language;
    private String country;
    private String contentRating;
    private Date releaseDate;
    private String urlItem;
    private List<String> genres;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        if (!List.of("Book", "Music", "Movie", "TV Series", "Video Game").contains(typeName)) {
            throw new IllegalArgumentException(
                "Media item must be of type Book, Music, Movie, TV Series or Video Game."
            );
        }
        this.typeName = typeName;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
