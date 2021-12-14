package com.is.istant;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoansFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView listView;
    private String loansNames[] = {
            "Prestito 1",
            "Prestito 2",
            "Prestito 3",
            "Prestito 4"
    };

    private Integer imageid[] = {
            R.drawable.gr_info,
            R.drawable.ic_user,
            R.drawable.gr_info,
            R.drawable.ic_user

    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /*
    ListView l;
    String loans_list[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };
    */

    public LoansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoansFragment newInstance(String param1, String param2) {
        LoansFragment fragment = new LoansFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_loans, container, false);

        /*
        // Setting header
        TextView textView = new TextView(getActivity());
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("Lista di prestiti"); */

        /*
        l = rootView.findViewById(R.id.loans_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, loans_list);
        l.setAdapter(arr);
        */

        ListView listView=(ListView)rootView.findViewById(android.R.id.list);

        CustomLoansList customLoansList = new CustomLoansList(getActivity(), loansNames, imageid);
        listView.setAdapter(customLoansList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity().getApplicationContext(),"You Selected "+ " as Country",Toast.LENGTH_SHORT).show();        }
        });

        FloatingActionButton fab = rootView.findViewById(R.id.fab_loan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoansFragment.this.getActivity(), CreateLoanActivity.class));
            }
        });

        return rootView;
    }
}