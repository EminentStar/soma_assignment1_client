package com.somaseven.data;

/**
 * Created by eminentstar on 2016. 7. 21..
 */
public class CommentListData {
    private String email;
    private String content;

    public CommentListData(String content, String email) {
        this.content = content;
        this.email = email;
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
}
