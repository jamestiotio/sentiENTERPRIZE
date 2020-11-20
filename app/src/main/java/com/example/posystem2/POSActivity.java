package com.example.posystem2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class POSActivity extends AppCompatActivity{
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


        // Create data
        final WeekData weekData = new WeekData(10);

        // Create fragments and data-passing bundles
        Bundle bundleDay = new Bundle();
        bundleDay.putSerializable("data", weekData.getLatestDay());
        final Fragment dayFrag = new DayFragment();
        dayFrag.setArguments(bundleDay);

        Bundle bundleWeek = new Bundle();
        bundleWeek.putSerializable("data", weekData);
        final Fragment weekFrag = new WeekFragment();
        weekFrag.setArguments(bundleWeek);

        final Fragment monthFrag = new MonthFragment();
        monthFrag.setArguments(bundleWeek);

        System.out.println(weekData.getDayDataArrayList());

        // Set initial look
        openFragment(dayFrag);
        totalSales.setText(String.format("%,.2f", weekData.getLatestDay().getDayTotal()));

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(dayFrag);
                double totalDaySales = weekData.getLatestDay().getDayTotal();
                totalSales.setText(String.format("%,.2f", totalDaySales));

                // Set button
                int[] buttonOn = {1, 0, 0, 0};
                setButtons(buttons, buttonOn);
            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(weekFrag);
                double totalWeekSales = 0;
                int count = 0;

                while (count < 7 && count < weekData.getDayDataArrayList().size()){
                    totalWeekSales += weekData.getDayDataArrayList().get(count).getDayTotal();
                    count++;
                }
                totalSales.setText(String.format("%,.2f", totalWeekSales));

                // Set button
                int[] buttonOn = {0, 1, 0, 0};
                setButtons(buttons, buttonOn);
            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(monthFrag);
                double totalMonthSales = 0;
                int count = 0;

                while (count < 30 && count < weekData.getDayDataArrayList().size()){
                    totalMonthSales += weekData.getDayDataArrayList().get(count).getDayTotal();
                    count++;
                }
                totalSales.setText(String.format("%,.2f", totalMonthSales));

                // Set button
                int[] buttonOn = {0, 0, 1, 0};
                setButtons(buttons, buttonOn);
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
}
