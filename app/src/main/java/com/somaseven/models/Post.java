package com.somaseven.models;

/**
 * Created by eminentstar on 2016. 7. 19..
 */
public class Post {
    private String email;
    private String title;
    private String content;

    public Post() {}

    public Post(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
