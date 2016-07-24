package com.somaseven.hweach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.somaseven.adapters.CommentListAdapter;
import com.somaseven.data.PrivateData;
import com.somaseven.models.Comment;
import com.somaseven.models.CommentResult;
import com.somaseven.models.PostResult;
import com.somaseven.network.RestClient;

import java.util.ArrayList;
import java.util.List;

public class DetailPostActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textTitle, textAuthor, textInterests, textComments, textContent;
    private ButtonRectangle suggestBtn, commentBtn;
    private EditText editComment;
    private ListView commentListView;

    private PostResult postResult;
    private PrivateData privateData = PrivateData.getInstance();
    private Comment comment;

    private CommentListAdapter commentListAdapter;
    private ArrayList<CommentResult> commentResultsList = new ArrayList<>();

    RestClient restClient = RestClient.getInstance();

    RestClient.CommentResultReadyCallback callback = new RestClient.CommentResultReadyCallback() {
        @Override
        public void resultReady(List<CommentResult> commentResults) {
            for(CommentResult commentResult: commentResults){
                commentResultsList.add(commentResult);
            }
            ((CommentListAdapter)commentListAdapter).notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        //UI
        textTitle = (TextView) findViewById(R.id.detailPostPage_text_title);
        textAuthor = (TextView) findViewById(R.id.detailPostPage_text_author);
        textInterests = (TextView) findViewById(R.id.detailPostPage_text_interests);
        textComments = (TextView) findViewById(R.id.detailPostPage_text_comments);
        textContent = (TextView) findViewById(R.id.detailPostPage_text_content);

        editComment = (EditText) findViewById(R.id.detailPostPage_edit_comment);

        suggestBtn = (ButtonRectangle) findViewById(R.id.detailPostPage_btn_suggest);
        suggestBtn.setOnClickListener(this);

        commentBtn = (ButtonRectangle) findViewById(R.id.detailPostPage_btn_comment);
        commentBtn.setOnClickListener(this);

        postResult = (PostResult) getIntent().getParcelableExtra("postResult");

        commentListAdapter = new CommentListAdapter(this, R.layout.comment_list_row, commentResultsList);
        restClient.setCommentCallback(callback);

        commentListView = (ListView) findViewById(R.id.detailPostPage_listView);
        commentListView.setAdapter(commentListAdapter);

        //Data Binding
        textTitle.setText(postResult.getTitle());
        textAuthor.setText(postResult.getEmail());
        textContent.setText(postResult.getContent());

        if(isAuthorOfArticle()){
            suggestBtn.setVisibility(View.GONE);
        }

        comment = new Comment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        commentResultsList.clear();
        restClient.getComments(postResult.getPostId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detailPostPage_btn_suggest:
                Intent intent = new Intent(getApplicationContext(), SuggestTutoringActivity.class);
                intent.putExtra("postResult", postResult);
                startActivity(intent);
                break;
            case R.id. detailPostPage_btn_comment:
                if(editComment.getText().toString().equals(""))
                    break;

                comment.setPostId(postResult.getPostId());
                comment.setEmail(privateData.getEmail());
                comment.setContent(editComment.getText().toString());

                restClient.createComment(getApplicationContext(), comment, postResult);
                break;
        }
    }

    private boolean isAuthorOfArticle(){
        return privateData.getEmail().toString().equals(postResult.getEmail().toString());
    }
}
