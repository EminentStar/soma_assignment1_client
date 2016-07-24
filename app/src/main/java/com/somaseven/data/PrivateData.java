package com.somaseven.data;

import com.facebook.AccessToken;

/**
 * Created by eminentstar on 2016. 7. 19..
 */
public class PrivateData {
    private static PrivateData instance;

    private String email;
    private String name;
    private String intro;
    private AccessToken accessToken;
    private String phoneNumber;
    private String gcmToken;


    public static PrivateData getInstance() {
        if (instance == null) {
            instance = new PrivateData();
        }
        return instance;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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
}
