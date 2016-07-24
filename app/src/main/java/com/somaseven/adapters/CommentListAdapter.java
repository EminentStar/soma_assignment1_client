package com.somaseven.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.somaseven.models.CommentResult;
import com.somaseven.hweach.R;

import java.util.ArrayList;

/**
 * Created by eminentstar on 2016. 7. 21..
 */
public class CommentListAdapter extends ArrayAdapter<CommentResult> {
    private Context context;
    private int layoutResourceID;
    private ArrayList<CommentResult> commentResultArrayList;

    public CommentListAdapter(Context context, int layoutResourceID, ArrayList<CommentResult> commentResultArrayList) {
        super(context, layoutResourceID, commentResultArrayList);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.commentResultArrayList = commentResultArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);
        }

        TextView tvTextEmail = (TextView) row.findViewById(R.id.comment_row_text_email);
        TextView tvTextContent = (TextView) row.findViewById(R.id.comment_row_text_content);

        tvTextEmail.setText(commentResultArrayList.get(position).getEmail().toString());
        tvTextContent.setText(commentResultArrayList.get(position).getContent());

        return row;
    }
}
