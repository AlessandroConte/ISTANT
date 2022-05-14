package com.example.istant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import com.example.istant.model.Loan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass containing the list of the created loans
 */
public class LoansFragment extends Fragment {

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // GUI
    private ListView loanslistview;
    private ArrayAdapter<Loan> adapter;
    private Button newLoan;
    private ArrayList<Loan> loanArrayList;
    private ProgressDialog pd;
    private Context context;
    private Switch switchMyLoan;
    private SwipeRefreshLayout refreshLayout;

    // other
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // CONSTRUCTOR
    public LoansFragment() {}


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

        View rootView = inflater.inflate(R.layout.loans_fragment, container, false);
        context = container.getContext();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loanslistview = rootView.findViewById(R.id.listView_fragmentloans);
        newLoan = rootView.findViewById(R.id.fragmentloans_btnNewLoan);
        switchMyLoan = rootView.findViewById(R.id.switch_loans);
        refreshLayout = rootView.findViewById(R.id.swipeRefresh_fragmentLoans);
        adapter = new LoanAdapter(context, new ArrayList<Loan>());
        loanArrayList = new ArrayList<Loan>();

        loanslistview.setAdapter(adapter);
        loanslistview.setClickable(true);

        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage(getString(R.string.fragmentloans_loading));

        newLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CreateNewLoanActivity.class));
            }
        });

        pd.show();
        displayLoans();

        switchMyLoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    adapter.clear();
                    loanArrayList.clear();
                    displayUserLoans();

                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            adapter.clear();
                            loanArrayList.clear();
                            displayUserLoans();
                            refreshLayout.setRefreshing(false);
                        }
                    });

                }
                else {
                    adapter.clear();
                    loanArrayList.clear();
                    displayLoans();

                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            adapter.clear();
                            loanArrayList.clear();
                            displayLoans();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                loanArrayList.clear();
                displayLoans();
                refreshLayout.setRefreshing(false);
            }
        });

        loanslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), VisualizeLoansActivity.class);
                intent.putExtra("loan", loanArrayList.get(i));
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    // This method is used to display all the created loans
    private void displayLoans() {
        db.collection("loan")
                .orderBy("dateEnd", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String id = document.getId();
                    Timestamp dateEnd = document.getTimestamp("dateEnd");
                    Timestamp dateStart = document.getTimestamp("dateStart");
                    String description = document.get("description").toString();
                    int isTaken = Integer.parseInt(document.get("isTaken").toString());
                    String nameLoan = document.get("nameLoan").toString();
                    String photoLoan = document.get("photoLoan").toString();
                    String takenUser = document.get("takenUser").toString();
                    String uid = document.get("uid").toString();

                    if (dateEnd.toDate().after(new Date())) {
                        Loan loan = new Loan(id, dateStart, dateEnd, photoLoan, description, nameLoan, isTaken, takenUser, uid);
                        loanArrayList.add(loan);
                    }

                    if (pd.isShowing()){
                        pd.dismiss();
                    }
                }
                adapter.clear();
                adapter.addAll(loanArrayList);
            }
        });
    }

    // This method is used to display all the loans taken by the logged user
    private void displayUserLoans () {
        db.collection("loan")
                .whereEqualTo("takenUser", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                Timestamp dateStart = document.getTimestamp("dateStart");
                                Timestamp dateEnd = document.getTimestamp("dateEnd");
                                String description = document.getData().get("description").toString();;
                                int isTaken = Integer.parseInt(document.get("isTaken").toString());
                                String nameLoan = document.getData().get("nameLoan").toString();
                                String photoLoanObj = document.getData().get("photoLoan").toString();
                                String takenUser = document.get("takenUser").toString();
                                String uid = document.getData().get("uid").toString();

                                if (dateEnd.toDate().after(new Date())) {
                                    Loan loan = new Loan(id, dateStart, dateEnd, photoLoanObj, description, nameLoan, isTaken, takenUser, uid);
                                    loanArrayList.add(loan);
                                }
                            }
                            adapter.clear();
                            adapter.addAll(loanArrayList);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Adapter for the ListView
    private class LoanAdapter extends ArrayAdapter<Loan> {
        ArrayList<Loan> loans;

        public LoanAdapter (@NonNull Context context, ArrayList<Loan> loans){
            super(context, 0, loans);
            this.loans = loans;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_loan, parent, false);
            }

            TextView loanName = convertView.findViewById(R.id.listadapter_loanName);
            View layout = convertView.findViewById(R.id.rowLayout_loanId);
            Loan loan = loanArrayList.get(position);

            if (loan.getIsTaken() == 0) {
                if (loan.getUid().equals(auth.getCurrentUser().getUid())) {
                    layout.setBackgroundColor(Color.WHITE);
                }
                else {
                    layout.setBackgroundColor(Color.parseColor("#77dd77"));
                }
            }
            else {
                if (loan.getIsTaken() == 1 && loan.getTakenUser().equals(auth.getCurrentUser().getUid())) {
                    layout.setBackgroundColor(Color.parseColor("#FDFD96"));
                }
                else {
                    layout.setBackgroundColor(Color.parseColor("#ff6961"));
                }
            }
            loanName.setText(loan.getNameLoan());

            return convertView;
        }
    }
}