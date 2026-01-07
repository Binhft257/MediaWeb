package com.javaweb.model.request;

public class UpdateTrackingRequest {
    private String status;   // PLAN_TO_WATCH | WATCHING | COMPLETED
    private String comment;  // optional
    private Integer rating;  // optional

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
}
