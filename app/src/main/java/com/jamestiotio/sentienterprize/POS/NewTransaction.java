package com.jamestiotio.sentienterprize.POS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamestiotio.sentienterprize.POSActivity;
import com.jamestiotio.sentienterprize.R;
import java.util.ArrayList;
import java.util.HashMap;

public class NewTransaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NewItemFragment.OnCompleteListener {
    String[] transTypeArray = {"Card", "Cash" };
    private static DatabaseReference db;
    HashMap<String, Integer> inventoryFull;
    TransactionSingle newTrans;
    ArrayList<Item> newItemList;
    Button newItemButton;
    Button newTransButton;
    ListView itemListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trans);

        // find widgets by IDs
        newItemButton = findViewById(R.id.newItemButton);
        newTransButton = findViewById(R.id.newTransButton);
        itemListView = findViewById(R.id.listView);

        // initialise database
        db = FirebaseDatabase.getInstance().getReference().child("RealApparel");

        // create new transaction
        newTrans = new TransactionSingle();

        // initialise data structures
        newItemList = new ArrayList<>();
        inventoryFull = new HashMap<>();

        // Set spinner for trans type
        Spinner spinTransType = (Spinner) findViewById(R.id.transTypeSelect);
        spinTransType.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transTypeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTransType.setAdapter(adapter);


        // new item button
        newItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewItemFragment newItemDialog = new NewItemFragment();
                newItemDialog.show(getSupportFragmentManager(),"Item Selection");
            }
        });

        newTransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newItemList.size() != 0){
                    newTrans.setItemList(newItemList);
                    newTrans.setTransTotal(calculateTransTotal(newItemList));
                    // update database
                    dbUpdate(newTrans);

                    Intent intent = new Intent(NewTransaction.this, POSActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getBaseContext(), "Nothing to add!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String type = String.valueOf(parent.getItemAtPosition(pos));
        newTrans.setTransType(type);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onComplete(Item item, HashMap<String, Integer> inventoryFull) {
        // After the dialog fragment completes, it calls this callback.
        // use the string here
        newItemList.add(item);
        this.inventoryFull = inventoryFull;
        showList(newItemList);
    }

    public void showList(ArrayList<Item> itemList){
        AddNewListAdapter adapter = new AddNewListAdapter(this, itemList);
        itemListView.setAdapter(adapter);
    }

    public double calculateTransTotal(ArrayList<Item> l){
        double total = 0;
        for (Item i:l){
            total += i.getUnitPrice() * i.getQuantity();
        }
        return total;
    }

    public void dbUpdate(TransactionSingle newTrans){
        // update transactions
        db.child("transactions").child(newTrans.getCode()).child("datetime").setValue(newTrans.getDatetime());
        db.child("transactions").child(newTrans.getCode()).child("transTotal").setValue(newTrans.getTransTotal());
        db.child("transactions").child(newTrans.getCode()).child("timestamp").setValue(newTrans.getTimestamp());
        db.child("transactions").child(newTrans.getCode()).child("transType").setValue(newTrans.getTransType());
        for(Item item : newTrans.getItemList()){
            db.child("transactions").child(newTrans.getCode()).child("itemList").child(item.getName()).setValue(item);

            // update inventory
            Integer newStockQuantity = inventoryFull.get(item.getName()) - item.getQuantity();
            db.child("inventory").child("items").child(item.getName()).child("amount").setValue(newStockQuantity);

        }
    }
}
