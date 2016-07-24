package com.somaseven.network;

import com.somaseven.models.Comment;
import com.somaseven.models.CommentResult;
import com.somaseven.models.GcmRequestInput;
import com.somaseven.models.InterestResult;
import com.somaseven.models.Post;
import com.somaseven.models.PostResult;
import com.somaseven.models.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by eminentstar on 2016. 7. 19..
 */
public interface APIService {
    @GET("/users")
    Call<JsonObject> getCountOfUsers();

    @GET("/users/{email}")
    Call<User> getUserInfo(@Path("email") String email);

    @POST("/users/common")
    Call<JsonObject> createCommonUser(@Body User user);

    @PUT("/users/common")
    Call<User> modifyCommonUser(@Body User user);

    @POST("/users/common/login")
    Call<JsonObject> isValidateInfo(@Body User user);

    @POST("/users/fb")
    Call<User> isValidateFbInfo(@Body User user);

    @POST("/posts/article")
    Call<JsonObject> createArticle(@Body Post post);

    //게시글 리스트 출력
    @GET("/posts/article")
    Call<List<PostResult>> getArticleList();

    @GET("/posts/article/{email}")
    Call<List<PostResult>> getArticleListOfUser(@Path("email") String email);

    @POST("/interests")
    Call<JsonObject> createInterest(@Body GcmRequestInput gcmRequestInput);

    @GET("/interests/{postId}")
    Call<List<InterestResult>> getInterestListOfArticle(@Path("postId") int postId);

    //GCM 전송
    @POST("/interests/gcm")
    Call<JsonObject> sendGcmWithRefreshingDB(@Body GcmRequestInput gcmRequestInput);

    @POST("/comments")
    Call<JsonObject> createComment(@Body Comment comment);

    @GET("/comments/{postId}")
    Call<List<CommentResult>> getCommentList(@Path("postId") int postId);
}
