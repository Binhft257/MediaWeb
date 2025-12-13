package com.javaweb.model.request;

public class UserRatingRequest  {
    private Integer ratingValue;

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating value should be between 1 and 5.");
        }
        this.ratingValue = ratingValue;
    }
}