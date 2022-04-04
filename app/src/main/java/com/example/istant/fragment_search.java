package com.example.istant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Needed to show the list of users
    ListView listview;
    ArrayList<User> userArrayList;


    ListView l;
    String users_list[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_cerca.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_search newInstance(String param1, String param2) {
        fragment_search fragment = new fragment_search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        l = rootView.findViewById(R.id.search_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, users_list);
        l.setAdapter(arr);

        return rootView;

         */

        // LIST OF INSTRUCTIONS NEEDED TO VIEW THE LIST OF PARTICIPANTS TO THE EVENT

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // here I get the reference to the list view I created in the xml file
        listview = rootView.findViewById(R.id.search_list);

        // here is the data I'm going to feed the list, all contained in the userArrayList
        String[] name = {"Anthony","Leonard","Lucas","Albert","Mike","Michael","Toa","Ivana","Nicholas"};
        String[] phoneNo = {"7656610000","9999043232","7834354323","9876543211","5434432343",
                "9439043232","7534354323","6545543211","7654432343"};
        userArrayList = new ArrayList<>();
        for(int i = 0;i< name.length;i++){
            User user = new User(name[i],"","",phoneNo[i],"",R.drawable.ic_user);
            userArrayList.add(user);
        }

        // Now I'm going to declare the adapter.
        // The adapter is a Java class that allows us to view a list of "complex" objects. These objects are formed by multiple elements.
        listadapter_user listadapter = new listadapter_user( getActivity(), userArrayList );

        // now we associate the listadapter with the listview
        listview.setAdapter( listadapter );

        return rootView;
    }
}