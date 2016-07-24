package com.somaseven.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.somaseven.models.InterestResult;
import com.somaseven.hweach.R;

import java.util.ArrayList;

/**
 * Created by eminentstar on 2016. 7. 22..
 */
public class InterestListAdapter extends ArrayAdapter<InterestResult> {
    private Context context;
    private int layoutResourceID;
    private ArrayList<InterestResult> interestResultArrayList;

    public InterestListAdapter(Context context, int layoutResourceID, ArrayList<InterestResult> interestResultArrayList) {
        super(context, layoutResourceID, interestResultArrayList);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.interestResultArrayList= interestResultArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);
        }

        TextView tvTextEmail = (TextView) row.findViewById(R.id.interest_row_text_email);
        TextView tvTextDescription = (TextView) row.findViewById(R.id.interest_row_text_description);

        tvTextEmail.setText(interestResultArrayList.get(position).getEmail().toString());
        tvTextDescription.setText(interestResultArrayList.get(position).getDescription().toString());

        return row;
    }
}
