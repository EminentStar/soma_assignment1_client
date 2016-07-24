package com.somaseven.models;

/**
 * Created by eminentstar on 2016. 7. 24..
 */
public class GcmRequestInput {
    private int postId;
    private String studentEmail;
    private String tutorEmail;
    private String description;

    public GcmRequestInput(int postId, String studentEmail, String tutorEmail) {
        this.postId = postId;
        this.studentEmail = studentEmail;
        this.tutorEmail = tutorEmail;
    }

    public GcmRequestInput(int postId, String studentEmail, String tutorEmail, String description) {
        this.postId = postId;
        this.studentEmail = studentEmail;
        this.tutorEmail = tutorEmail;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }
}
