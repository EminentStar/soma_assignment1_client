package com.somaseven.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eminentstar on 2016. 7. 18..
 */
public class PostResult implements Parcelable {
    private int postId;
    private String email;
    private String title;
    private String content;
    private int isComplete;
    private int interestCount;
    private int commentCount;

    public PostResult() {
    }

    protected PostResult(Parcel in) {
        postId = in.readInt();
        email = in.readString();
        title = in.readString();
        content = in.readString();
        isComplete = in.readInt();
        interestCount = in.readInt();
        commentCount = in.readInt();
    }

    public static final Creator<PostResult> CREATOR = new Creator<PostResult>() {
        @Override
        public PostResult createFromParcel(Parcel in) {
            return new PostResult(in);
        }

        @Override
        public PostResult[] newArray(int size) {
            return new PostResult[size];
        }
    };

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

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getInterestCount() {
        return interestCount;
    }

    public void setInterestCount(int interestCount) {
        this.interestCount = interestCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(postId);
        parcel.writeString(email);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeInt(isComplete);
        parcel.writeInt(interestCount);
        parcel.writeInt(commentCount);
    }

}
