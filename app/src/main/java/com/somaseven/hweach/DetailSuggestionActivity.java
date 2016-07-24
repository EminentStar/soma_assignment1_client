package com.somaseven.hweach;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.somaseven.adapters.InterestListAdapter;
import com.somaseven.data.PrivateData;
import com.somaseven.models.GcmRequestInput;
import com.somaseven.models.Interest;
import com.somaseven.models.InterestResult;
import com.somaseven.models.PostResult;
import com.somaseven.network.RestClient;

import java.util.ArrayList;
import java.util.List;

public class DetailSuggestionActivity extends AppCompatActivity {

    private TextView textTitle;
    private ListView interestListView;

    private ArrayList<InterestResult> interestResultArrayList = new ArrayList<>();
    private InterestListAdapter interestListAdapter;

    RestClient restClient = RestClient.getInstance();

    RestClient.InterestResultReadyCallback callback = new RestClient.InterestResultReadyCallback() {
        @Override
        public void resultReady(List<InterestResult> interestResults) {
            for(InterestResult interestResult: interestResults){
                interestResultArrayList.add(interestResult);
            }
            ((InterestListAdapter)interestListAdapter).notifyDataSetChanged();
        }
    };

    private PrivateData privateData = PrivateData.getInstance();

    private PostResult postResult;
    private Interest tutorInfo;
    private GcmRequestInput gcmRequestInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_suggestion);

        textTitle = (TextView) findViewById(R.id.detailTutoringPage_title);
        interestListView = (ListView) findViewById(R.id.detailTutoringPage_listView);
        postResult = (PostResult) getIntent().getParcelableExtra("postResult");
        textTitle.setText(postResult.getTitle());

        interestListAdapter = new InterestListAdapter(this, R.layout.interest_list_row, interestResultArrayList);
        restClient.setInterestCallback(callback);
        interestListView.setAdapter(interestListAdapter);

        tutorInfo = new Interest();
        tutorInfo.setPostId(postResult.getPostId());

        interestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                InterestResult item = (InterestResult) adapterView.getItemAtPosition(position);

                AlertDialog.Builder applyTutoringDialog = new AlertDialog.Builder(DetailSuggestionActivity.this);
                applyTutoringDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //fcm 푸시 메시지를 통해서 튜터와 본인에게 상대방의 전화번호를 전달한다.
                        Toast.makeText(getApplicationContext(), "OK clicked", Toast.LENGTH_SHORT).show();
                        //Server로 본 튜터링 요청글의 postId, 그리고 튜터링 신청한 사람의 email, description을 전송한다.
                        //전송 로직에선 튜터링을 받는 사람과 튜터 두명에게 타인의 이름과 전화번호를 받는다.
                        gcmRequestInput = new GcmRequestInput(postResult.getPostId(), privateData.getEmail(), interestResultArrayList.get(position).getEmail());
                        restClient.sendGcmToStudentAndTutor(getApplicationContext(), gcmRequestInput);

                    }
                });
                applyTutoringDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                applyTutoringDialog.setMessage( interestResultArrayList.get(position).getEmail() + "님께 튜터링을 신청하시겠습니까?");
                applyTutoringDialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        interestResultArrayList.clear();
        restClient.getInterestListOfArticle(postResult.getPostId());
    }
}
