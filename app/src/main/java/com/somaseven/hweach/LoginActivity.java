package com.somaseven.hweach;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.somaseven.data.PrivateData;
import com.somaseven.models.User;
import com.somaseven.network.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // UI
    private AutoCompleteTextView mEmailView;
    private EditText editEmail, editPwd;
    private ButtonRectangle btnEmailLogin, btnSignUp;
    private View mLoginFormView;

    RestClient restClient = RestClient.getInstance();

    //페이스북 관련 객체
    LoginButton facebookLoginBtn;
    CallbackManager callbackManager;

    //계정 정보
    private User loginInfo;
    private PrivateData privateData = PrivateData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        LoginManager.getInstance().logOut();

        //기기의 gcm token을 받아온다.
        new GcmTokenLoaderAsyncTask().execute();

        //로그인 폼 셋팅
        mLoginFormView = findViewById(R.id.login_form);
        editEmail = (EditText)findViewById(R.id.login_edit_email);
        editPwd = (EditText)findViewById(R.id.login_edit_pwd);
        btnSignUp = (ButtonRectangle) findViewById(R.id.login_btn_register);
        btnEmailLogin = (ButtonRectangle) findViewById(R.id.login_btn_login);

        btnSignUp.setOnClickListener(this);
        btnEmailLogin.setOnClickListener(this);

        loginInfo = new User();

        callbackManager = CallbackManager.Factory.create();
        facebookLoginBtn = (LoginButton) findViewById(R.id.login_btn_facebook);
        facebookLoginBtn.setReadPermissions("email");
        facebookLoginBtn.setReadPermissions(Arrays.asList("user_status"));

        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //페이스북 회원의 기본 정보를 받아옴.
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.d("ACCESS", "hitted onCompleted");
                                try {
                                    if(!object.isNull("email")){
                                        privateData.setEmail(object.getString("email"));
                                    }else{
                                        privateData.setEmail(object.getString("name"));
                                    }

                                    privateData.setName(object.getString("name"));
                                    String id = object.getString("id");
                                    // 로그인 시도
                                    restClient.isValidateFbInfo(
                                            getApplicationContext(),
                                            new User(privateData.getEmail(), privateData.getName(), privateData.getGcmToken()) );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "페이스북 로그인을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    LoginManager.getInstance().logOut();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.login_btn_login:
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                attemptLogin();
                break;
            case R.id.login_btn_register:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void attemptLogin() {
        if(isEditTextEmpty(editEmail)){
            Toast.makeText(LoginActivity.this, "email을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }else if(isEditTextEmpty(editPwd)){
            Toast.makeText(LoginActivity.this, "pwd을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        loginInfo.setEmail(editEmail.getText().toString());
        loginInfo.setPwd(editPwd.getText().toString());
        loginInfo.setGcmToken(privateData.getGcmToken());
        restClient.isValidateUser(getApplicationContext(), loginInfo);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private boolean isEditTextEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        editEmail.setText("");
        editPwd.setText("");
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private class GcmTokenLoaderAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {


            /*gcm*/
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            //GCM 토큰 생성을 위한 변수
            String default_senderId;
            String scope;
            String token = "";
            try {
                // GCM 앱을 등록하고 획득한 설정파일인 google-services.json을 기반으로 SenderID를 자동으로 가져온다.
                default_senderId = getString(R.string.gcm_defaultSenderId);
                // GCM 기본 scope는 "GCM"이다.
                scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                // Instance ID에 해당하는 토큰을 생성하여 가져온다.
                token = instanceID.getToken(default_senderId, scope, null);

                Log.i("SignUpActivity", "GCM Registration Token: " + token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);
            privateData.setGcmToken(token);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }
}