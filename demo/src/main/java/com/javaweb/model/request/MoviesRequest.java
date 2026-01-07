package com.javaweb.model.request;

public class MoviesRequest extends MediaCreateRequest{
    private String producers;
    private String director;
    private String cast;
    private Integer runTimeMinutes;
    
    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Integer getRunTimeMinutes() {
        return runTimeMinutes;
    }

    public void setRunTimeMinutes(Integer runTimeMinutes) {
        this.runTimeMinutes = runTimeMinutes;
    }
}
