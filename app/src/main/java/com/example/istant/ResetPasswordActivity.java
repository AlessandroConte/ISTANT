package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText userPassword, userConfirmPassword;
    Button savePasswordBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userPassword = findViewById(R.id.newUserPsw);
        userConfirmPassword = findViewById(R.id.confirmPsw);

        user = FirebaseAuth.getInstance().getCurrentUser();

        savePasswordBtn = findViewById(R.id.newPswButton);
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userPassword.getText().toString().isEmpty()){
                    userPassword.setError("La nuova password deve essere fornita");
                    return;
                }

                if (userConfirmPassword.getText().toString().isEmpty()){
                    userConfirmPassword.setError("La nuova password deve essere fornita");
                    return;
                }

                if (!userPassword.getText().toString().equals(userConfirmPassword.getText().toString())){
                    userConfirmPassword.setError("Le password sono diverse");
                    return;
                }

                user.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPasswordActivity.this, "Password aggiornata!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}