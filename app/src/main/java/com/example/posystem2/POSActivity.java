package com.example.posystem2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class POSActivity extends AppCompatActivity{
    private static DatabaseReference db;
    private ArrayList<TransactionSingle> transList = new ArrayList<>();

    // Instantiate widgets
    TextView totalSales;
    Button dayButton;
    Button weekButton;
    Button monthButton;
    Button rangeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        //
        db = FirebaseDatabase.getInstance().getReference().child("RealApparel").child("transactions");

        // Read from the database
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    transList.add(new TransactionSingle(snapshot));
                }
                updateListView(transList);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB Error", "Failed to read value.", error.toException());
            }
        });
    }

    private void openFragment(final Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flFrag, fragment);
        ft.commit();
    }

    private void setButtons(ArrayList<Button> buttons, int[] l){
        for (int i = 0; i < buttons.size(); i++){
            if (l[i] == 0){
                buttons.get(i).setTextColor(Color.parseColor("#FFFFFF"));
            }else{
                buttons.get(i).setTextColor(Color.parseColor("#000000"));
            }
        }
    }

    private void updateListView(final ArrayList<TransactionSingle> transList){
        TransactionClassification classified = new TransactionClassification(transList);
        final ArrayList<TransactionSingle> dayList = classified.getDayList();
        final ArrayList<TransactionSingle> weekList = classified.getWeekList();
        final ArrayList<TransactionSingle> monthList = classified.getMonthList();

        // Find widgets by IDs
        totalSales = findViewById(R.id.TotalSales);
        dayButton = findViewById(R.id.dayButton);
        weekButton = findViewById(R.id.weekButton);
        monthButton = findViewById(R.id.monthButton);
        rangeButton = findViewById(R.id.rangeButton);

        // Array of 4 action buttons
        final ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(dayButton);
        buttons.add(weekButton);
        buttons.add(monthButton);
        buttons.add(rangeButton);

        // data pipelining
        Bundle bundleDay = new Bundle();
        bundleDay.putSerializable("data", dayList);
        Bundle bundleWeek = new Bundle();
        bundleWeek.putSerializable("data", weekList);
        Bundle bundleMonth = new Bundle();
        bundleMonth.putSerializable("data", monthList);
        Bundle bundleData = new Bundle();
        bundleData.putSerializable("data", transList);

        // create fragments
        final Fragment dayFrag = new DayFragment();
        dayFrag.setArguments(bundleDay);

        final Fragment weekFrag = new WeekFragment();
        weekFrag.setArguments(bundleWeek);

        final Fragment monthFrag = new MonthFragment();
        monthFrag.setArguments(bundleMonth);

        final Fragment rangeFrag = new RangeFragment();
        rangeFrag.setArguments(bundleData);

        // Set initial look
        openFragment(dayFrag);
        totalSales.setText(getTotal(dayList));

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(dayFrag);
                totalSales.setText(getTotal(dayList));

                // Set button
                int[] buttonOn = {1, 0, 0, 0};
                setButtons(buttons, buttonOn);
            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(weekFrag);
                totalSales.setText(getTotal(weekList));

                // Set button
                int[] buttonOn = {0, 1, 0, 0};
                setButtons(buttons, buttonOn);
            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(monthFrag);
                totalSales.setText(getTotal(monthList));

                // Set button
                int[] buttonOn = {0, 0, 1, 0};
                setButtons(buttons, buttonOn);
            }
        });

        rangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(rangeFrag);
                totalSales.setText(getTotal(transList));

                // Set button
                int[] buttonOn = {0, 0, 0, 1};
                setButtons(buttons, buttonOn);
            }
        });
    }

    public String getTotal(ArrayList<TransactionSingle> l){
        double total = 0;

        for (TransactionSingle t : l){
            total += t.getTransTotal();
        }

        return String.format("%,.2f", total);
    }
}
