package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  This activity allows the user either to log in the application, to create a new account or to re-set the password if forgotten
 */
public class LoginActivity extends AppCompatActivity {

    // GUI
    private Button createAccountBtn, loginButton, forgetPasswordButton;
    private EditText username, password;
    private FirebaseAuth fAuth;
    private AlertDialog.Builder reset_alert;
    private LayoutInflater inflater;
    private AlertDialog.Builder builder;

    // METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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

        fAuth = FirebaseAuth.getInstance();
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        createAccountBtn = findViewById(R.id.accountButton);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        forgetPasswordButton = findViewById(R.id.forgetPasswordButton);
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start alert dialog
                View v = inflater.inflate(R.layout.reset_pop,null);
                reset_alert.setTitle(getString(R.string.loginactivity_alertpasswortreset_name))
                        .setMessage(getString(R.string.loginactivity_alertpasswortreset_message))
                        .setPositiveButton(getString(R.string.loginactivity_reset), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // validate the email address
                                EditText email = v.findViewById(R.id.resetEmailPop);
                                if (email.getText().toString().isEmpty()){
                                    email.setError(getString(R.string.loginactivity_alertpasswortreset_givemail));
                                    return;
                                }

                                // send the reset link
                                fAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LoginActivity.this, getString(R.string.loginactivity_alertpasswortreset_emailsent),Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton(getString(R.string.loginactivity_alertpasswortreset_cancel), null)
                        .setView(v)
                        .create().show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()){
                    username.setError(getString(R.string.loginactivity_loginbutton_noemail));
                    return;
                }

                if (password.getText().toString().isEmpty()){
                    password.setError(getString(R.string.loginactivity_loginbutton_nopassword));
                    return;
                }

                // login user
                fAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

    protected void onStart(){
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}