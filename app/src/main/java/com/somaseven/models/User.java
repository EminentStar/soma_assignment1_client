package com.somaseven.models;

/**
 * Created by eminentstar on 2016. 7. 19..
 */
public class User {
    private String email;
    private String name;
    private String pwd;
    private String introduction;
    private String createTime;
    private int isFacebook;
    private String phoneNumber;
    private String gcmToken;

    public User() {
    }

    public User(String email, String name, String gcmToken) {
        this.email = email;
        this.name = name;
        this.gcmToken = gcmToken;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public int getIsFacebook() {
        return isFacebook;
    }
    public void setIsFacebook(int isFacebook) {
        this.isFacebook = isFacebook;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "createTime='" + createTime + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", introduction='" + introduction + '\'' +
                ", isFacebook=" + isFacebook +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gcmToken='" + gcmToken + '\'' +
                '}';
    }
}
