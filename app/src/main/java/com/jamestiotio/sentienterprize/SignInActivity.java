package com.jamestiotio.sentienterprize;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamestiotio.sentienterprize.models.User;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Button buttonLogIn, buttonSignUp;
    private EditText editTextViewEmail, editTextViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            onAuthSuccess(user);
        }

        // Views
        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);
        setProgressBar(R.id.progressBar);

        // Click listeners
        buttonLogIn.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void logIn() {
        Log.d(TAG, "logIn");

        String email = editTextViewEmail.getText().toString();
        String password = editTextViewPassword.getText().toString();

        if (!validateForm(email, password)) {
            return;
        }

        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication Failed :( Contact admin for help", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signUp() {
        Log.d(TAG, "signUp");

        String email = editTextViewEmail.getText().toString();
        String password = editTextViewPassword.getText().toString();

        if (!validateForm(email, password)) {
            return;
        }

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Registration Failed :( Contact admin for help", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        Toast.makeText(getApplicationContext(), "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();
        Intent intentMainToOption = new Intent(SignInActivity.this, MenuActivity.class);
        startActivity(intentMainToOption);
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm(String email, String password) {
        boolean result = true;

        if (TextUtils.isEmpty(email)) {
            editTextViewEmail.setError(getText(R.string.error_email_required));
            result = false;
        }
        else {
            editTextViewEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            editTextViewPassword.setError(getText(R.string.error_password_required));
            result = false;
        }
        else {
            editTextViewPassword.setError(null);
        }

        if (password.length() < 8) {
            editTextViewPassword.setError(getText(R.string.error_password_longer));
            result = false;
        }
        else {
            editTextViewPassword.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("RealApparel").child("users").child(userId).setValue(user);
    }
    // [END basic_write]

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonLogIn) {
            logIn();
        } else if (i == R.id.buttonSignUp) {
            signUp();
        }
    }
}
