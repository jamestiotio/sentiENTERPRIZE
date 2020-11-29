package com.example.posystem2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    Button buttonLogOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        buttonLogOut = findViewById(R.id.buttonLogOut);

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null) {
                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(), "Logged out " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent intentProfileToMain = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intentProfileToMain);
                }
            }
        });
    }
}