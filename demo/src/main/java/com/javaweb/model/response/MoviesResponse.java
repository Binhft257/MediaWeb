package com.javaweb.model.response;

public class MoviesResponse extends MediaSearchResponse{
    private String producers;
    private String director;
    private String cast;
    private int runTimeMinutes;

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

    public int getRunTimeMinutes() {
        return runTimeMinutes;
    }

    public void setRunTimeMinutes(int runTimeMinutes) {
        this.runTimeMinutes = runTimeMinutes;
    }
}
