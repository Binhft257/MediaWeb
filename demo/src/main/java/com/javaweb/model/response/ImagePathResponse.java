package com.javaweb.model.response;

public class ImagePathResponse {
    private String path; // lưu DB
    private String url;  // FE dùng ngay

    public ImagePathResponse(String path, String url) {
        this.path = path;
        this.url = url;
    }

    public String getPath() { return path; }
    public String getUrl() { return url; }
}
