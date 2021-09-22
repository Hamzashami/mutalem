package com.app.mutalem.model;

public class Artical {
    private int id;
    private String imageUrl;
    private String title;
    private String content;

    public Artical(int id, String imageUrl, String title, String content) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
