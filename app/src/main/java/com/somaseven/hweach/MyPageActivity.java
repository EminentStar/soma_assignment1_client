package com.somaseven.hweach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;
import com.somaseven.data.PrivateData;
import com.somaseven.models.User;
import com.somaseven.network.RestClient;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editName, editPhoneNumber, editIntro;
    private ButtonRectangle modifyBtn, cancelBtn, tutoringBtn;

    private User user;

    private PrivateData privateData = PrivateData.getInstance();

    RestClient restClient = RestClient.getInstance();

    RestClient.ModificationCommonUserReadyCallback updateCallback = new RestClient.ModificationCommonUserReadyCallback() {
        @Override
        public void resultReady(User user) {
            editEmail.setText(user.getEmail());
            editName.setText(user.getName());
            editIntro.setText(user.getIntroduction());
            editPhoneNumber.setText(user.getPhoneNumber());

        }
    };

    RestClient.UserInfoReadyCallback userInfoCallback = new RestClient.UserInfoReadyCallback() {
        @Override
        public void resultReady(User user) {
            editEmail.setText(user.getEmail());
            editName.setText(user.getName());
            editIntro.setText(user.getIntroduction());
            editPhoneNumber.setText(user.getPhoneNumber());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        user = new User();

        editEmail = (EditText) findViewById(R.id.myPage_edit_email);
        editName = (EditText) findViewById(R.id.myPage_edit_name);
        editIntro = (EditText) findViewById(R.id.myPage_edit_intro);
        editPhoneNumber = (EditText) findViewById(R.id.myPage_edit_phoneNumber);

        modifyBtn = (ButtonRectangle) findViewById(R.id.myPage_btn_modify);
        cancelBtn = (ButtonRectangle) findViewById(R.id.myPage_btn_cancel);
        tutoringBtn = (ButtonRectangle) findViewById(R.id.myPage_btn_tutoring);

        editEmail.setEnabled(false);
        editName.setEnabled(false);

        modifyBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        tutoringBtn.setOnClickListener(this);

        editEmail.setText(privateData.getEmail());
        editName.setText(privateData.getName());
        if(privateData.getIntro() != null)
            editIntro.setText(privateData.getIntro());
        if(privateData.getPhoneNumber() != null)
            editPhoneNumber.setText(privateData.getPhoneNumber());

        restClient.setModificationCommonUserCallback(updateCallback);
        restClient.setUserInfoCallback(userInfoCallback);

        restClient.getUserInfo(privateData.getEmail());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myPage_btn_modify:
                if(editName.getText().toString().equals("")){
                    break;
                }else if(editIntro.getText().toString().equals("")){
                    break;
                }else if(editPhoneNumber.getText().toString().equals("")){
                    break;
                }
                user.setEmail(editEmail.getText().toString());
                user.setName(editName.getText().toString());
                user.setIntroduction(editIntro.getText().toString());
                user.setPhoneNumber(editPhoneNumber.getText().toString());
                user.setGcmToken(privateData.getGcmToken());

                restClient.modifyCommonUser(getApplicationContext(), user);
                break;
            case R.id.myPage_btn_tutoring:
                Intent intent = new Intent(getApplicationContext(), TutoringManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.myPage_btn_cancel:
                finish();
                break;
        }
    }
}
