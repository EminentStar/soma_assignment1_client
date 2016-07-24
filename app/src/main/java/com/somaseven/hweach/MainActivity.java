package com.somaseven.hweach;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.gc.materialdesign.views.ButtonRectangle;
import com.somaseven.adapters.PostListAdapter;
import com.somaseven.data.PrivateData;
import com.somaseven.models.PostResult;
import com.somaseven.network.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView postListView;
    private ButtonRectangle marketBtn, myPageBtn, writeBtn;
    private TextView textUserCount, textArticleCount;
    PostListAdapter postListAdapter;

    ArrayList<PostResult> postResultList = new ArrayList<>();

    private PrivateData privateData = PrivateData.getInstance();

    RestClient restClient = RestClient.getInstance();

    RestClient.PostResultReadyCallback callback = new RestClient.PostResultReadyCallback() {
        @Override
        public void resultReady(List<PostResult> postResults) {
            for(PostResult postResult: postResults){
                postResultList.add(postResult);
            }
            textArticleCount.setText(Integer.toString(postResultList.size()));
            ((PostListAdapter) postListAdapter).notifyDataSetChanged();
        }
    };

    RestClient.UserCountReadyCallback userCallback = new RestClient.UserCountReadyCallback() {
        @Override
        public void resultReady(int userCount) {
            textUserCount.setText(Integer.toString(userCount));
        }
    };

    String email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI
        marketBtn = (ButtonRectangle) findViewById(R.id.main_btn);
        myPageBtn = (ButtonRectangle) findViewById(R.id.myPage_btn);
        writeBtn = (ButtonRectangle) findViewById(R.id.write_btn);

        marketBtn.setOnClickListener(this);
        myPageBtn.setOnClickListener(this);
        writeBtn.setOnClickListener(this);


        textUserCount = (TextView) findViewById(R.id.mainPage_text_userCount);
        textArticleCount = (TextView) findViewById(R.id.mainPage_text_articleCount);

        postListView = (ListView) findViewById(R.id.post_listView);
        postListAdapter = new PostListAdapter(this, R.layout.post_list_row, postResultList);

        restClient.setUserCallback(userCallback);
        restClient.setCallback(callback);

        postListView.setAdapter(postListAdapter);

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView tv = (TextView) view.findViewById(R.id.post_row_text_title);
                PostResult item = (PostResult) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), DetailPostActivity.class);
                intent.putExtra("postResult", item);
                startActivity(intent);
            }
        });

        email = "";
        name = "";
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.write_btn:
                intent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn:
                Toast.makeText(MainActivity.this, "새로고침하였습니다.", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.myPage_btn:
                intent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        postListAdapter.clear();
        restClient.getArticles();
        restClient.getCountOfUsers();
    }

    private class InfoLoader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            GraphRequest request = GraphRequest.newMeRequest(
                    privateData.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            try {
                                email = object.getString("email");
                                name = object.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), email + " + " + name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginManager.getInstance().logOut();
    }
}
