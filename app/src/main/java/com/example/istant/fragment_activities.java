package com.example.istant;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.example.istant.databinding.FragmentActivitiesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_activities#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_activities extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_activities() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_attivita.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_activities newInstance(String param1, String param2) {
        fragment_activities fragment = new fragment_activities();
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

        FragmentActivitiesBinding binding = FragmentActivitiesBinding.inflate(inflater, container, false);

        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);

        int[] imageId = {R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,
                R.drawable.ic_user, R.drawable.ic_user,R.drawable.ic_user};
        String[] name = {"Attivitàaaaaaaaaaaa1","Uscita al parcooooooooooooooooooo","Nuoto","Calcio pazzo in via delle rive ","Mike","Michael","Toa","Ivana"," "};
        String[] description = {"Descrizione","Andiamo al parco alle 5","Passaggio nuoto ore 3","Trasporto verso campo da calcio ore 6","prova",
                "i'm in meeting","Gotcha","Let's Go","RIGA DA SVUOTARE PER BELLEZZA"};
        String[] reviews = {"3.0/5","5.0/5","4.2/5","3.7/5","1.0/5",
                "3.0/5","2.7/5","4.5/5"," "};
        String[] phoneNo = {"7656610000","9999043232","7834354323","9876543211","5434432343",
                "9439043232","7534354323","6545543211","7654432343"};
        String[] country = {"United States","Russia","India","Israel","Germany","Thailand","Canada","France","Switzerland"};

        ArrayList<User> userArrayList = new ArrayList<>();

        for(int i = 0;i< imageId.length;i++){

            User user = new User(name[i],description[i],reviews[i],phoneNo[i],country[i],imageId[i]);
            userArrayList.add(user);

        }


        ListAdapter listAdapter  = new ListAdapter(getActivity(),userArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        //data shared with next activity
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(), activity_visualizeactivities.class);
                i.putExtra("name",name[position]);
                i.putExtra("phone",phoneNo[position]);
                i.putExtra("country",country[position]);
                i.putExtra("imageid",imageId[position]);
                startActivity(i);

            }
        });

        View view = inflater.inflate(R.layout.fragment_activities, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_activities);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), activity_create_activities.class);
                startActivity(i);
            }
        });



        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}