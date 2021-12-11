package com.is.istant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomLoansList extends ArrayAdapter {
    private String[] loansNames;
    private Integer[] imageid;
    private Activity context;


    public CustomLoansList(Activity context, String[] loansNames, Integer[] imageid) {
        super(context, R.layout.layout_view_adapter, loansNames);
        this.context = context;
        this.loansNames = loansNames;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.layout_view_adapter, null, true);
        TextView textViewList = (TextView) row.findViewById(R.id.list_text_loans);
        ImageView imageList = (ImageView) row.findViewById(R.id.list_image);
        textViewList.setText(loansNames[position]);
        imageList.setImageResource(imageid[position]);
        return  row;
    }
}
