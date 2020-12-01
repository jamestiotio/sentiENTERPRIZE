package com.example.posystem2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    Button buttonLogIn, buttonSignUp;
    EditText editTextViewEmail, editTextViewPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Toast.makeText(getApplicationContext(), "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();
            Intent intentMainToOption = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intentMainToOption);
        }

        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextViewEmail.getText().toString();
                String password = editTextViewPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    editTextViewEmail.setError(getText(R.string.error_email_required));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextViewPassword.setError(getText(R.string.error_password_required));
                    return;
                }

                if (password.length() < 8) {
                    editTextViewPassword.setError(getText(R.string.error_password_longer));
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();
                                    Intent intentMainToOption = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(intentMainToOption);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(), "Authentication Failed :( Contact admin for help", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextViewEmail.getText().toString();
                String password = editTextViewPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    editTextViewEmail.setError(getText(R.string.error_email_required));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextViewPassword.setError(getText(R.string.error_password_required));
                    return;
                }

                if (password.length() < 8) {
                    editTextViewPassword.setError(getText(R.string.error_password_longer));
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();
                                    Intent intentMainToOption = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(intentMainToOption);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(), "Registration Failed :( Contact admin for help", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}