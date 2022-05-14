package com.example.istant;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass containing the info of the group
 */
public class GroupInfoFragment extends Fragment {

    // other
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // CONSTRUCTOR
    public GroupInfoFragment() {}


    // METHODS

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.groupinfo_fragment, container, false);

        ImageView image = (ImageView)rootView.findViewById(R.id.gr_image);
        image.setImageResource(R.drawable.ic_class);

        return rootView;
    }
}