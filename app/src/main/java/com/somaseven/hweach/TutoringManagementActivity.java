package com.somaseven.hweach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.somaseven.adapters.PostListAdapter;
import com.somaseven.data.PrivateData;
import com.somaseven.models.PostResult;
import com.somaseven.network.RestClient;

import java.util.ArrayList;
import java.util.List;

public class TutoringManagementActivity extends AppCompatActivity {

    private ListView postListView;

    private PrivateData privateData = PrivateData.getInstance();

    private ArrayList<PostResult> postResultArrayList = new ArrayList<>();

    private PostListAdapter postListAdapter;

    RestClient restClient = RestClient.getInstance();

    RestClient.PostResultReadyCallback callback = new RestClient.PostResultReadyCallback() {
        @Override
        public void resultReady(List<PostResult> postResults) {
            for(PostResult postResult: postResults){
                postResultArrayList.add(postResult);
            }
            ((PostListAdapter)postListAdapter).notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoring_management);

        postListAdapter = new PostListAdapter(this, R.layout.post_list_row, postResultArrayList);
        postListView = (ListView) findViewById(R.id.tutoringMangePage_listView);

        restClient.setSpecificPostCallback(callback);
        postListView.setAdapter(postListAdapter);

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PostResult item = (PostResult) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), DetailSuggestionActivity.class);
                intent.putExtra("postResult", item);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        postListAdapter.clear();
        restClient.getArticleListOfUser(privateData.getEmail());
    }
}
