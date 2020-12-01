package com.example.posystem2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity {

    TextView textViewBranchName;
    TextView textViewCompanyId;
    TextView textViewProfit;
    TextView textViewProfitMonth;
    TextView textViewTransaction;
    TextView textViewTransactionMonth;
    TextView textViewOrder;
    TextView textViewOrderMonth;

    Button buttonLogOut;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Object data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("RealApparel").child("profile");

        textViewBranchName = findViewById(R.id.textViewBranchNameValue);
        textViewCompanyId = findViewById(R.id.textViewCompanyIdValue);
        textViewProfit = findViewById(R.id.textViewProfitValue);
        textViewProfitMonth = findViewById(R.id.textViewProfitMonthValue);
        textViewTransaction = findViewById(R.id.textViewTransactionValue);
        textViewTransactionMonth = findViewById(R.id.textViewTransactionMonthValue);
        textViewOrder = findViewById(R.id.textViewOrderValue);
        textViewOrderMonth = findViewById(R.id.textViewOrderMonthValue);
        buttonLogOut = findViewById(R.id.buttonLogOut);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("branchName")) {
                        textViewBranchName.setText(snapshot.getValue().toString());
                    }
                    if (snapshot.getKey().equals("companyId")) {
                        textViewCompanyId.setText(snapshot.getValue().toString());
                    }
                    if (snapshot.getKey().equals("orders")) {
                        textViewOrder.setText(snapshot.getValue().toString());
                    }
                    if (snapshot.getKey().equals("ordersMonth")) {
                        textViewOrderMonth.setText(snapshot.getValue().toString());
                    }
                    if (snapshot.getKey().equals("profit")) {
                        textViewProfit.setText(snapshot.getValue().toString());
                    }
                    if (snapshot.getKey().equals("profitMonth")) {
                        textViewProfitMonth.setText(snapshot.getValue().toString());
                    }
                    if (snapshot.getKey().equals("transactions")) {
                        textViewTransaction.setText(snapshot.getValue().toString());
                    }
                    if (snapshot.getKey().equals("transactionsMonth")) {
                        textViewTransactionMonth.setText(snapshot.getValue().toString());
                    }
                    Log.i("print", snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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