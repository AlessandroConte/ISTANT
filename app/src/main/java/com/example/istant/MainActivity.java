package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Alert dialog
    private AlertDialog.Builder builder;
    private AlertDialog.Builder builder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottombar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        //NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.fragment_groupinfo, R.id.fragment_loans, R.id.fragment_search, R.id.fragment_activities, R.id.fragment_user).build();
        // on the line above I need to add the references to the different fragments

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        auth = FirebaseAuth.getInstance();
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();


        // Alert dialog that prompts the user to view the guide page
        if ( ((JustLoggedIn) this.getApplication()).getJustLogged() == true ) {
            builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.mainactivity_guidealert_title))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.mainactivity_guidealert_positivebutton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), GuideActivity.class));
                        }
                    })
                    .setNegativeButton(getString(R.string.mainactivity_guidealert_negativebutton), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }
                    );
            builder.show();
        }
    }

    // Function that creates the menu in the upper right corner of the screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Function that manages what is done when you open the options menu and click on one of the items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Reset password option
        if (item.getItemId() == R.id.resetPassword){
            startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
        }

        // Delete account option
        if (item.getItemId() == R.id.deleteAccountMenu){
            reset_alert.setTitle(getString(R.string.mainactivity_deleteaccountalert))
                    .setMessage(getString(R.string.mainactivity_deleteaccountalert_message))
                    .setPositiveButton(getString(R.string.mainactivity_deleteaccountalert_positivebutton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = auth.getCurrentUser();
                            deleteDatabaseDocument(db, "user", user.getUid());
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, getString(R.string.mainactivity_accountdeleted), Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton(getString(R.string.mainactivity_deleteaccountalert_negativebutton), null)
                    .create().show();
        }

        // Logout option
        if (item.getItemId() == R.id.logout){
            reset_alert.setTitle(getString(R.string.mainactivity_logoutalert))
                    .setMessage(getString(R.string.mainactivity_logoutalert_message))
                    .setPositiveButton(getString(R.string.mainactivity_logoutalert_positivebutton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.mainactivity_logoutalert_negativebutton), null)
                    .create().show();
        }

        // User Guide option
        if (item.getItemId() == R.id.userguide) {
            startActivity(new Intent(getApplicationContext(), GuideActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public static void deleteDatabaseDocument(FirebaseFirestore db, String collectionName,
                                              String idDocument) {
        db.collection(collectionName).document(idDocument).delete();
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