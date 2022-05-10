package com.example.istant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.istant.model.Child;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class SettingsManageChildrenActivity extends AppCompatActivity {

    private ListView childrenlistview;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ArrayAdapter<Child> adapter;
    private ArrayList<Child> children;
    private Button newChild;

    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_managechildren_activity);

        // Check if there is connectivity
        if(isConnectingToInternet(getApplicationContext()) == false)   {
            builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.checkinternetconnectivity_alertnointernetmessage))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.checkinternetconnectivity_alertnointernetmessage_checkagain), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .setNegativeButton( getString(R.string.checkinternetconnectivity_alertnointernetmessage_close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }
                    );
            builder.show();
        }


        // This allows to the actionbar to have a back button pointing to the last activity you visited
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.activitysettingsmanagechildren_managechildren));
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        childrenlistview = findViewById(R.id.settings_managechildren_childrenlist);
        newChild = findViewById(R.id.btn_createchildren);

        adapter = new ChildrenAdapter(this, new ArrayList<Child>());
        childrenlistview.setAdapter(adapter);
        childrenlistview.setClickable(true);
        children = new ArrayList<Child>();

        displayChildren();

        newChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsManageChildrenActivity.this, CreateNewChildActivity.class);
                startActivity(intent);
            }
        });

        childrenlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Child child = children.get(i);
                Log.d("ID = ", "" + child.getId());
                deleteDatabaseDocument(db, "child", child.getId());
                children.remove(i);
                return true;
            }
        });
    }

    // This function allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in the settings activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayChildren () {
        db.collection("child")
                .whereEqualTo("uid", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                Timestamp dateBorn = document.getTimestamp("bornDate");
                                int gender = Integer.parseInt(document.getData().get("gender").toString());
                                String name = document.getData().get("name").toString();
                                String surname = document.getData().get("surname").toString();

                                Child child = new Child(id, null, dateBorn, gender, "", name, surname, "");
                                children.add(child);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                        adapter.clear();
                        adapter.addAll(children);
                    }
                });
    }

    public static void deleteDatabaseDocument(FirebaseFirestore db, String collectionName, String idDocument) {
        db.collection(collectionName).document(idDocument).delete();
    }

    private class ChildrenAdapter extends ArrayAdapter<Child> {
        ArrayList<Child> children;

        public ChildrenAdapter(@NonNull Context context, ArrayList<Child> children) {
            super(context, 0, children);
            this.children = children;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_children,parent, false);
            }

            TextView childName = convertView.findViewById(R.id.listadapter_childName);
            TextView childSurname = convertView.findViewById(R.id.listadapter_childSurname);
            TextView childGender = convertView.findViewById(R.id.listadapter_childGender);
            TextView childAge = convertView.findViewById(R.id.listadapter_childDate);

            Child child = children.get(position);

            childName.setText(child.getName());
            childSurname.setText(child.getSurname());
            if(child.getGender() == 1){
                childGender.setText("M");
            }
            else{
                childGender.setText("F");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
            Timestamp bornDate = child.getDateBorn();
            String time = String.valueOf(bornDate.getSeconds());
            long timestampLong = Long.parseLong(time)*1000;
            Date d = new Date(timestampLong);
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            /*
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int date = c.get(Calendar.DATE);
             */
            childAge.setText(dateFormat.format(d));

            return convertView;
        }
    }

    // Function that checks if there is internet connection
    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            String s = "true";
            Log.i("true wifi",s);
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            String s = "true";
            Log.i("true mobileNetwork",s);
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            String s = "true activeNetwork";
            Log.i("true",s);
            return true;
        }
        String s = "false";
        Log.i("false",s);
        return false;
    }
}