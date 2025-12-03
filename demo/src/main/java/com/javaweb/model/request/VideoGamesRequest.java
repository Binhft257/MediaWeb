package com.javaweb.model.request;

public class VideoGamesRequest extends MediaCreateRequest{
    private String developer;
    private String publisher;
    private String platform;
    private String minRequirement;

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getMinRequirement() {
        return minRequirement;
    }

    public void setMinRequirement(String minRequirement) {
        this.minRequirement = minRequirement;
    }
}
