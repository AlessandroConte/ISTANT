package com.example.istant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.istant.model.Loan;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_loans#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_loans extends Fragment implements ListAdapter_loans.OnLoanListener{

    private RecyclerView recyclerView;
    private ArrayList<Loan> loanArrayList;
    private ListAdapter_loans adapterLoans;
    private FirebaseFirestore db;
    private ProgressDialog pd;
    private Context context;

    private Button newLoan;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_loans() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_prestiti.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_loans newInstance(String param1, String param2) {
        fragment_loans fragment = new fragment_loans();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loans, container, false);
        context = container.getContext();

        loanArrayList = new ArrayList<Loan>();
        adapterLoans = new ListAdapter_loans(context, loanArrayList,this);

        recyclerView = rootView.findViewById(R.id.recyclerView_fragmentLoans);
        recyclerView.setAdapter(adapterLoans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        newLoan = rootView.findViewById(R.id.fragmentloans_btnNewLoan);
        newLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, activity_createNewLoan.class));
            }
        });

        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Fetching data..");
        pd.show();

        db = FirebaseFirestore.getInstance();

        db.collection("loan").orderBy("dateStart", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                            Log.d("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                loanArrayList.add(dc.getDocument().toObject(Loan.class));
                            }
                            adapterLoans.notifyDataSetChanged();
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                        }
                    }
                });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onLoanClick(int position) {
        Intent intent = new Intent(context, activity_visualizeloans.class);
        //intent.putExtra("loan", loanArrayList.get(position));
        // TODO: quando invochiamo la nuova schermata, dobbiamo portarci le info del prestito cliccato
        startActivity(intent);
        // Log.d("RecyclerView Item","position = " + position);
    }
}