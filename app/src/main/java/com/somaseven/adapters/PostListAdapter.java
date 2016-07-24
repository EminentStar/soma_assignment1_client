package com.somaseven.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.somaseven.models.PostResult;
import com.somaseven.hweach.R;

import java.util.ArrayList;

/**
 * Created by eminentstar on 2016. 7. 18..
 */
public class PostListAdapter extends ArrayAdapter<PostResult>{
    private Context context;
    private int layoutResourceID;
    private ArrayList<PostResult> postResultArrayList;


    public PostListAdapter(Context context, int layoutResourceID, ArrayList<PostResult> postResultArrayList) {
        super(context, layoutResourceID, postResultArrayList);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.postResultArrayList = postResultArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);
        }

        TextView tvTextTitle = (TextView) row.findViewById(R.id.post_row_text_title);
        TextView tvTextInterest = (TextView) row.findViewById(R.id.post_row_text_interest);
        TextView tvTextComments = (TextView) row.findViewById(R.id.post_row_text_comments);

        tvTextTitle.setText(postResultArrayList.get(position).getTitle());
        tvTextInterest.setText(Integer.toString(postResultArrayList.get(position).getInterestCount()));
        tvTextComments.setText(Integer.toString(postResultArrayList.get(position).getCommentCount()));

        return row;
    }
}
