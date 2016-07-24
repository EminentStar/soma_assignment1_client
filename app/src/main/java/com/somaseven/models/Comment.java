package com.somaseven.models;

/**
 * Created by eminentstar on 2016. 7. 21..
 */
public class Comment {
    private String email;
    private int postId;
    private String content;

    public Comment() {
    }

    public Comment(String email, int postId, String content) {
        this.email = email;
        this.postId = postId;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
