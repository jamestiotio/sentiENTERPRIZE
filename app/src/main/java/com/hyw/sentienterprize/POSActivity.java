package com.hyw.sentienterprize;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// temp formatting method for fake data
import static com.hyw.sentienterprize.TestData.formatPrice;


public class POSActivity extends AppCompatActivity{
    // Instantiate widgets
    TextView totalPrice;
    Button dayButton;
    Button weekButton;
    Button monthButton;
    Button rangeButton;
    ExpandableListView transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_o_s);

        // Find widgets by IDs
        totalPrice = findViewById(R.id.total);
        dayButton = findViewById(R.id.dayButton);
        weekButton = findViewById(R.id.weekButton);
        monthButton = findViewById(R.id.monthButton);
        rangeButton = findViewById(R.id.rangeButton);
        transactions = findViewById(R.id.elvItems);

        // Create fake data
        TestData testData = new TestData();
        totalPrice.setText(formatPrice(testData.getTotalPrice()));

        // Create an adapter (BaseExpandableListAdapter) with the data above
        final ListViewAdapter listViewAdapter = new ListViewAdapter(this, testData);
        // defines the ExpandableListView adapter
        transactions.setAdapter(listViewAdapter);
    }
}
