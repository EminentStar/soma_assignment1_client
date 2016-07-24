package com.somaseven.models;

/**
 * Created by eminentstar on 2016. 7. 21..
 */
public class Interest {
    private int postId;
    private String email;
    private String description;

    public Interest() {
    }

    public Interest(int postId, String email, String description) {
        this.postId = postId;
        this.email = email;
        this.description = description;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
