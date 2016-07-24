package com.somaseven.models;

/**
 * Created by eminentstar on 2016. 7. 21..
 */
public class CommentResult {
    private int commentId;
    private int postId;
    private String email;
    private String content;

    public CommentResult(int commentId, int postId, String email, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.email = email;
        this.content = content;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
