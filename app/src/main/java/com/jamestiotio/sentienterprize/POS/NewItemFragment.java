package com.jamestiotio.sentienterprize.POS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamestiotio.sentienterprize.R;
import java.util.ArrayList;
import java.util.HashMap;


public class NewItemFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private static DatabaseReference db;
    ArrayList<String> itemsInInventory;
    ArrayList<Integer> itemsStock;
    ArrayList<Double> itemsUnitPrice;
    HashMap<String, Integer> inventoryFull;
    Spinner itemSelectSpinner;
    TextView newItemPrice;
    TextView newItemStock;
    TextView warningText;
    EditText quantity;
    String selectedItem;
    Double selectedUnitPrice;
    Integer selectedStock;
    Button addButton;
    Button cancelButton;
    private OnCompleteListener mListener;

    public interface OnCompleteListener {
        public void onComplete(Item item, HashMap<String, Integer> inventoryFull);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        View view = inflater.inflate(R.layout.fragment_new_item_dialogue, null);

        // Find widgets by IDs
        itemSelectSpinner = view.findViewById(R.id.itemSelect);
        newItemPrice = view.findViewById(R.id.newItemPrice);
        quantity = view.findViewById(R.id.quantitySelect);
        newItemStock = view.findViewById(R.id.newItemStock);
        addButton = view.findViewById(R.id.addButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        warningText = view.findViewById(R.id.warningText);

        // Initialise ArrayLists
        itemsInInventory = new ArrayList<>();
        itemsStock = new ArrayList<>();
        itemsUnitPrice = new ArrayList<>();
        inventoryFull = new HashMap<>();

        // get inventory list
        db = FirebaseDatabase.getInstance().getReference().child("RealApparel").child("inventory").child("items");
        // Read from the database
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    itemsInInventory.add(snapshot.getKey());
                    itemsStock.add(Integer.parseInt(snapshot.child("amount").getValue().toString()));
                    itemsUnitPrice.add((Double) snapshot.child("unitPrice").getValue());
                    inventoryFull.put(snapshot.getKey(),Integer.parseInt(snapshot.child("amount").getValue().toString()));
                }

                updateDialogue(itemsInInventory);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB Error", "Failed to read value.", error.toException());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewItemFragment.this.getDialog().cancel();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get input quantity
                try{
                    Integer inputQuantity = Integer.parseInt(quantity.getText().toString());
                    if (inputQuantity > selectedStock){
                        warningText.setText("Not enough stock available.");
                    }else{
                        Item newItem = new Item(selectedItem, inputQuantity, selectedUnitPrice);

                        // send back data
                        mListener.onComplete(newItem, inventoryFull);

                        // close dialogue
                        NewItemFragment.this.getDialog().dismiss();
                    }
                }catch(NumberFormatException ex){
                    warningText.setText("Illegal input format.");
                }
            }
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String item = String.valueOf(parent.getItemAtPosition(pos));

        // get current selection index
        int idx = itemsInInventory.indexOf(item);

        // set selected
        selectedItem = item;
        selectedUnitPrice = itemsUnitPrice.get(idx);
        selectedStock = itemsStock.get(idx);

        newItemPrice.setText(selectedUnitPrice.toString());
        newItemStock.setText(selectedStock.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void updateDialogue(ArrayList<String> names){

        // Set item selection spinner
        itemSelectSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapterDialogue = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, names);
        adapterDialogue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSelectSpinner.setAdapter(adapterDialogue);
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        try {
            this.mListener = (OnCompleteListener)c;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(c.toString() + " must implement OnCompleteListener");
        }
    }
}
