package com.somaseven.hweach;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.somaseven.data.PrivateData;
import com.somaseven.models.Post;
import com.somaseven.network.RestClient;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTitle, editContent;
    private ButtonRectangle registerBtn;
    private Post post;

    private PrivateData privateData = PrivateData.getInstance();

    RestClient restClient = RestClient.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //UI binding
        editTitle = (EditText) findViewById(R.id.writePage_edit_title);
        editContent = (EditText) findViewById(R.id.writePage_edit_content);
        registerBtn = (ButtonRectangle) findViewById(R.id.writePage_btn_register);

        registerBtn.setOnClickListener(this);

        post = new Post();
        post.setEmail(privateData.getEmail());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.writePage_btn_register:
                if(isEditTextEmpty(editTitle)){
                    Toast.makeText(WriteActivity.this, "title을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }else if(isEditTextEmpty(editContent)){
                    Toast.makeText(WriteActivity.this, "content 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                post.setTitle(editTitle.getText().toString());
                post.setContent(editContent.getText().toString());

                //writeArticleToServer();
                restClient.createArticle(getApplicationContext(), post);
                finish();
                break;
        }
    }

    private boolean isEditTextEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }
}
