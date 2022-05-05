package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    TextView tv_gdpr;
    EditText regNome, regCognome, regEmail, regPass, regConfPass;
    Button regButton,gotoLogin, gdpr;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regNome = findViewById(R.id.registrationName);
        regCognome = findViewById(R.id.registrationSurname);
        regEmail = findViewById(R.id.registrationEmail);
        regPass = findViewById(R.id.registrationPassword);
        regConfPass = findViewById(R.id.registrationConfPassword);
        regButton = findViewById(R.id.registrationButton);
        gotoLogin = findViewById(R.id.gotologinButton);
        tv_gdpr = findViewById(R.id.tv_gdpr);

        fAuth = FirebaseAuth.getInstance();

        tv_gdpr.setPaintFlags(tv_gdpr.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // extract the data from the form

                String nome = regNome.getText().toString();
                String cognome = regCognome.getText().toString();
                String email = regEmail.getText().toString();
                String pass = regPass.getText().toString();
                String confpass = regConfPass.getText().toString();

                if(nome.isEmpty()){
                    regNome.setError("Il nome è richiesto");
                    return;
                }
                if(cognome.isEmpty()){
                    regCognome.setError("Il cognome è richiesto");
                    return;
                }
                if(email.isEmpty()){
                    regEmail.setError("L'email è richiesta");
                    return;
                }
                if(pass.isEmpty()){
                    regPass.setError("La password è richiesta");
                    return;
                }
                if(confpass.isEmpty()){
                    regConfPass.setError("La password è richiesta");
                    return;
                }

                if (!pass.equals(confpass)){
                    regConfPass.setError("Le password devono essere uguali");
                    return;
                }

                Toast.makeText(RegistrationActivity.this, "I dati inseriti sono corretti", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // sen user to the next page
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tv_gdpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), activity_GDPR.class));
                finish();
            }
        });

    }
}