package com.javaweb.model.request;

public class MoviesRequest extends MediaCreateRequest{
    private String producers;
    private String directors;
    private String cast;
    private int runTimeMinutes;
    
    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public int getRunTimeMinutes() {
        return runTimeMinutes;
    }

    public void setRunTimeMinutes(int runTimeMinutes) {
        this.runTimeMinutes = runTimeMinutes;
    }
}
