package com.somaseven.hweach;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.somaseven.models.User;
import com.somaseven.network.RestClient;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SIGNUPACIVITY";

    private EditText editEmail, editName, editPwd, editPhoneNumber;
    private ButtonRectangle btnRegister;

    //GCM 토큰 생성을 위한 변수
    private String default_senderId;
    private String scope;
    private String token;

    RestClient restClient = RestClient.getInstance();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editEmail = (EditText) findViewById(R.id.signUp_edit_email);
        editName = (EditText) findViewById(R.id.signUp_edit_name);
        editPwd = (EditText) findViewById(R.id.signUp_edit_pwd);
        editPhoneNumber = (EditText) findViewById(R.id.signUp_edit_phoneNumber);

        btnRegister = (ButtonRectangle) findViewById(R.id.signUp_btn_register);

        btnRegister.setOnClickListener(this);
        user = new User();

        new GcmTokenLoaderAsyncTask().execute();
    }

    private boolean isEditTextEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUp_btn_register:
                if(isEditTextEmpty(editEmail)){
                    Toast.makeText(SignUpActivity.this, "email을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }else if(isEditTextEmpty(editName)){
                    Toast.makeText(SignUpActivity.this, "name을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }else if(isEditTextEmpty(editPwd)){
                    Toast.makeText(SignUpActivity.this, "pwd을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }else if(isEditTextEmpty(editPhoneNumber)){
                    Toast.makeText(SignUpActivity.this, "phone을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                user.setEmail(editEmail.getText().toString());
                user.setName(editName.getText().toString());
                user.setPwd(editPwd.getText().toString());
                user.setPhoneNumber(editPhoneNumber.getText().toString());

                user.setGcmToken(token);
                Log.d(TAG, user.toString());
                restClient.createCommonUser(getApplicationContext(), user);

                break;
        }
    }

    private class GcmTokenLoaderAsyncTask extends AsyncTask<Void, Void, String>{

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            token = s;
        }
    }
}
