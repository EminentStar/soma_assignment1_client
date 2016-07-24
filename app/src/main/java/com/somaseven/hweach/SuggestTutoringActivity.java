package com.somaseven.hweach;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;
import com.somaseven.data.PrivateData;
import com.somaseven.models.GcmRequestInput;
import com.somaseven.models.Interest;
import com.somaseven.models.PostResult;
import com.somaseven.network.RestClient;

public class SuggestTutoringActivity extends Activity implements View.OnClickListener{

    private ButtonRectangle suggestBtn, cancelBtn;
    private EditText editName, editDescription;

    private PrivateData privateData = PrivateData.getInstance();

    private RestClient restClient = RestClient.getInstance();

    private Interest suggestion;
    private PostResult postResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_suggest_tutoring);
        postResult = getIntent().getParcelableExtra("postResult");

        suggestion = new Interest();

        suggestBtn = (ButtonRectangle) findViewById(R.id.suggestPage_btn_suggest);
        cancelBtn = (ButtonRectangle) findViewById(R.id.suggestPage_btn_cancel);

        suggestBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        editName = (EditText) findViewById(R.id.suggestPage_edit_name);
        editDescription = (EditText) findViewById(R.id.suggestPage_edit_descrption);

        editName.setEnabled(false);
        editName.setText(privateData.getName());

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.suggestPage_btn_suggest:
                if(editDescription.getText().toString().equals(""))
                    break;

                suggestion.setPostId(postResult.getPostId());
                suggestion.setEmail(privateData.getEmail());
                suggestion.setDescription(editDescription.getText().toString());

                GcmRequestInput gcmRequestInput = new GcmRequestInput(postResult.getPostId(),
                        postResult.getEmail(),
                        privateData.getEmail(),
                        editDescription.getText().toString()
                );

                restClient.createInterest(getApplicationContext(), gcmRequestInput);
                break;
            case R.id.suggestPage_btn_cancel:
                finish();
                break;
        }
    }
}
