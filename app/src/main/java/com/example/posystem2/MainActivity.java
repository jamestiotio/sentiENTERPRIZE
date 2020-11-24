package com.example.posystem2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button buttonLogIn, buttonSignUp;
    EditText editTextViewEmail, editTextViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogIn = findViewById(R.id.buttonLogIn);
        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsecureAuth insecureAuth = new InsecureAuth();

                if (insecureAuth.isLegitUser(editTextViewEmail.getText().toString(), editTextViewPassword.getText().toString())) {
                    Intent intentMainToOption = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intentMainToOption);
                } else { // AUTH FAILED
                    Toast.makeText(getApplicationContext(), "Authentication Failed! Contact admin for help", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}