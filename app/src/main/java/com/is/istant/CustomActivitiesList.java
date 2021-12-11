package com.is.istant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomActivitiesList extends ArrayAdapter {
    private String[] activitiesNames;
    private String[] activitiesRates;
    private Integer[] imageid;
    private Activity context;


    public CustomActivitiesList(Activity context, String[] activitiesNames, String[] activitiesRates, Integer[] imageid) {
        super(context, R.layout.layout_view_adapter_activities, activitiesNames);
        this.context = context;
        this.activitiesNames = activitiesNames;
        this.activitiesRates = activitiesRates;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.layout_view_adapter_activities, null, true);
        TextView textViewList = (TextView) row.findViewById(R.id.list_text);
        TextView textViewList2 = (TextView) row.findViewById(R.id.rating_text);
        ImageView imageList = (ImageView) row.findViewById(R.id.list_image);
        textViewList.setText(activitiesNames[position]);
        textViewList2.setText(activitiesRates[position]);
        imageList.setImageResource(imageid[position]);
        return  row;
    }
}
