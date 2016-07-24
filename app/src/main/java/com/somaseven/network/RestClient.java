package com.somaseven.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.somaseven.data.PrivateData;
import com.somaseven.hweach.DetailPostActivity;
import com.somaseven.hweach.LoginActivity;
import com.somaseven.hweach.MainActivity;
import com.somaseven.hweach.TutoringManagementActivity;
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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by eminentstar on 2016. 7. 19..
 */
public class RestClient {
    //Twitter

    private static RestClient instance = null;

    private ResultReadyCallback callback;

    private PostResultReadyCallback postCallback;
    private PostResultReadyCallback specificPostCallback;
    private UserCountReadyCallback userCallback;
    private ModificationCommonUserReadyCallback modificationCommonUserCallback;
    private CommentResultReadyCallback commentCallback;
    private UserInfoReadyCallback userInfoReadyCallback;
    private InterestResultReadyCallback interestCallback;

    private static final String BASE_URL = "http://52.78.16.164/";
    private APIService service;
    List<PostResult> articles = null;
    List<PostResult> articlesOfUser = null;
    List<CommentResult> comments = null;
    List<InterestResult> interests = null;

    boolean success = false;
    int userCount = 0;

    private User modifiedUser = new User();
    private User userInfo;

    private PrivateData privateData = PrivateData.getInstance();

    private RestClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(APIService.class);
    }

    public int getCountOfUsers(){
        success = false;
        userCount = 0;
        Call<JsonObject> call = service.getCountOfUsers();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                success = response.isSuccessful();
                if(success){
                    userCount = response.body().getAsJsonPrimitive("userCount").getAsInt();
                    userCallback.resultReady(userCount);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
        return userCount;
    }

    public List<PostResult> getArticles() {
        Call<List<PostResult>> articleList = service.getArticleList();
        articleList.enqueue(new Callback<List<PostResult>>() {
            @Override
            public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                if (response.isSuccessful()) {
                    articles = response.body();
                    postCallback.resultReady(articles);
                }
            }

            @Override
            public void onFailure(Call<List<PostResult>> call, Throwable t) {

            }
        });
        return articles;
    }
    public List<PostResult> getArticleListOfUser(final String email){
        Call<List<PostResult>> articleList = service.getArticleListOfUser(email);
        articleList.enqueue(new Callback<List<PostResult>>() {
            @Override
            public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                if(response.isSuccessful()){
                    articlesOfUser = response.body();
                    specificPostCallback.resultReady(articlesOfUser);
                }
            }

            @Override
            public void onFailure(Call<List<PostResult>> call, Throwable t) {

            }
        });
        return articlesOfUser;
    }

    public List<InterestResult> getInterestListOfArticle(final int postId){
        Call<List<InterestResult>> call = service.getInterestListOfArticle(postId);
        call.enqueue(new Callback<List<InterestResult>>() {
            @Override
            public void onResponse(Call<List<InterestResult>> call, Response<List<InterestResult>> response) {
                if(response.isSuccessful()){
                    interests = response.body();
                    interestCallback.resultReady(interests);
                }
            }

            @Override
            public void onFailure(Call<List<InterestResult>> call, Throwable t) {

            }
        });
        return interests;
    }

    public void setCallback(PostResultReadyCallback callback) { this.postCallback = callback; }

    public void setSpecificPostCallback(PostResultReadyCallback callback) { this.specificPostCallback = callback; }

    public void setUserCallback(UserCountReadyCallback callback) { this.userCallback = callback; }

    public void setModificationCommonUserCallback(ModificationCommonUserReadyCallback callback) { this.modificationCommonUserCallback = callback; }

    public void setInterestCallback(InterestResultReadyCallback callback ) { this.interestCallback = callback;}

    public void setUserInfoCallback(UserInfoReadyCallback callback ) { this.userInfoReadyCallback= callback;}


    public boolean createArticle(final Context context, Post post){
        Call<JsonObject> p = service.createArticle(post);
        success = false;
        p.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                success = response.isSuccessful();
                if(success){
                    boolean isSucceeded = response.body().getAsJsonPrimitive("isSucceeded").getAsBoolean();
                    if(isSucceeded){
                        Toast.makeText(context, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "글을 등록할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.w("REST", t.getMessage());
                Toast.makeText(context, "서버가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return success;
    }

    public boolean createCommonUser(final Context context, User user){
        Call<JsonObject> u = service.createCommonUser(user);
        success = false;
        u.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                success = response.isSuccessful();
                if(success){
                    boolean isSucceeded = response.body().getAsJsonPrimitive("isSucceeded").getAsBoolean();
                    if(isSucceeded){
                        Toast.makeText(context, "회원가입되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "회원가입을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.w("REST", t.getMessage());
                Toast.makeText(context, "서버가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return success;
    }

    public boolean createInterest(final Context context, final GcmRequestInput gcmRequestInput){
        Call<JsonObject> call = service.createInterest(gcmRequestInput);
        success = false;
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                success = response.isSuccessful();
                if(success){
                    boolean isSucceeded = response.body().getAsJsonPrimitive("isSucceeded").getAsBoolean();
                    if(isSucceeded){
                        Toast.makeText(context, "튜터링 제안 완료.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "튜터링 제안을 하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "서버가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                //메인 화면으로 돌아간다.
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "서버가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                //메인 화면으로 돌아간다.
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return success;
    }

    public User modifyCommonUser(final Context context, final User user){
        Call<User> call = service.modifyCommonUser(user);
        success = false;

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                success = response.isSuccessful();
                if(success){
                    Toast.makeText(context, "정보 수정에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                    User result = response.body();
                    privateData.setEmail(result.getEmail());
                    privateData.setName(result.getName());
                    privateData.setIntro(result.getIntroduction());
                    privateData.setPhoneNumber(result.getPhoneNumber());

                    modifiedUser.setEmail(result.getEmail());
                    modifiedUser.setName(result.getName());
                    modifiedUser.setIntroduction(result.getIntroduction());
                    modifiedUser.setPhoneNumber(result.getPhoneNumber());
                    modificationCommonUserCallback.resultReady(modifiedUser);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "정보 수정에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return modifiedUser;
    }

    public User getUserInfo(String email){
        Call<User> call = service.getUserInfo(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userInfo = response.body();
                userInfoReadyCallback.resultReady(userInfo);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        return userInfo;
    }

    public boolean isValidateFbInfo(final Context context, User user){
        Call<User> call = service.isValidateFbInfo(user);
        success = false;
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                success = response.isSuccessful();
                if(success){
                    try{
                        User result = response.body();
                        privateData.setEmail(result.getEmail());
                        privateData.setName(result.getName());
                        privateData.setIntro(result.getIntroduction());
                        privateData.setPhoneNumber(result.getPhoneNumber());
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Toast.makeText(context, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "서버가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
            }
        });
        return success;
    }

    public boolean isValidateUser(final Context context, User user){
        Call<JsonObject> call = service.isValidateInfo(user);
        success = false;
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                success = response.isSuccessful();
                if(success){
                    boolean isSucceeded = response.body().getAsJsonPrimitive("isSucceeded").getAsBoolean();
                    if(isSucceeded) {
                        String name = response.body().getAsJsonPrimitive("name").getAsString();
                        String email = response.body().getAsJsonPrimitive("email").getAsString();
                        String phoneNumber = response.body().getAsJsonPrimitive("phoneNumber").getAsString();
                        /*gcm*/
                        String gcmToken = response.body().getAsJsonPrimitive("gcmToken").getAsString();

                        privateData.setEmail(email);
                        privateData.setName(name);
                        privateData.setPhoneNumber(phoneNumber);
                        /*gcm*/
                        privateData.setGcmToken(gcmToken);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Toast.makeText(context, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return success;
    }

    public boolean createComment(final Context context, Comment comment, final PostResult postResult){
        Call<JsonObject> c = service.createComment(comment);
        success = false;
        c.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                success = response.isSuccessful();
                if(success){
                    boolean isSucceeded = response.body().getAsJsonPrimitive("isSucceeded").getAsBoolean();
                    if(isSucceeded){
                        Toast.makeText(context, "댓글을 달았습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "댓글을 다는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(context, DetailPostActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("postResult", postResult);
                    context.startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "서버가 원활하지 않습니다.[댓글]", Toast.LENGTH_SHORT).show();
            }
        });
        return success;
    }

    public List<CommentResult> getComments(final int postId){
        final Call<List<CommentResult>> commentList = service.getCommentList(postId);
        success = false;
        commentList.enqueue(new Callback<List<CommentResult>>() {
            @Override
            public void onResponse(Call<List<CommentResult>> call, Response<List<CommentResult>> response) {
                success = response.isSuccessful();
                if(success){
                    comments = response.body();
                    commentCallback.resultReady(comments);
                }
            }

            @Override
            public void onFailure(Call<List<CommentResult>> call, Throwable t) {
            }
        });
        return comments;
    }

    public boolean sendGcmToStudentAndTutor(final Context context, final GcmRequestInput gcmRequestInput){
        Call<JsonObject> call = service.sendGcmWithRefreshingDB(gcmRequestInput);
        success = false;
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                /*
                 * 2건의 GCM 전송 및
                 * 해당 Post에 관련된 모든 Interest row를 지우고,
                 * 해당 Post의 isComplete를 1로 바꾸는 작업을
                 * 모두 완료하였을때 JsonObject의 isSucceeded 엘레멘트에 true가 들어오게 한다.
                 * */
                if(response.isSuccessful()){
                    boolean isSucceeded = response.body().getAsJsonPrimitive("isSucceeded").getAsBoolean();
                    if(isSucceeded){
                        Toast.makeText(context, "튜터링 매칭이 성공적으로 이뤄졌습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, TutoringManagementActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }else{
                        Toast.makeText(context, "튜터링 매칭에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "서버가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    public void setCommentCallback(CommentResultReadyCallback callback) {this.commentCallback = callback; }

    public static RestClient getInstance() {
        if(instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public interface ResultReadyCallback{
        public void resultReady(List<PostResult> postResults);
    }

    public interface PostResultReadyCallback{
        public void resultReady(List<PostResult> postResults);
    }

    public interface UserCountReadyCallback{
        public void resultReady(int userCount);
    }

    public interface UserInfoReadyCallback{
        public void resultReady(User user);
    }

    public interface ModificationCommonUserReadyCallback{
        public void resultReady(User user);
    }

    public interface CommentResultReadyCallback{
        public void resultReady(List<CommentResult> commentResults);
    }

    public interface InterestResultReadyCallback{
        public void resultReady(List<InterestResult> interestResults);
    }



}
