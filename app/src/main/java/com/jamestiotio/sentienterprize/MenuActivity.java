package com.jamestiotio.sentienterprize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button buttonPOS;
    Button buttonInventory;
    Button buttonDemand;
    Button buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        buttonPOS = findViewById(R.id.buttonPOS);
        buttonInventory = findViewById(R.id.buttonInventory);
        buttonDemand = findViewById(R.id.buttonDemand);
        buttonProfile = findViewById(R.id.buttonProfile);

        buttonPOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainToOption = new Intent(MenuActivity.this, POSActivity.class);
                startActivity(intentMainToOption);

            }
        });

        buttonInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainToOption = new Intent(MenuActivity.this, InventoryActivity.class);
                startActivity(intentMainToOption);

            }
        });

        buttonDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainToOption = new Intent(MenuActivity.this, DemandActivity.class);
                startActivity(intentMainToOption);

            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainToOption = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intentMainToOption);

            }
        });
    }

}