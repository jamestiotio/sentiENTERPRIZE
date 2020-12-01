package com.jamestiotio.sentienterprize;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamestiotio.sentienterprize.models.Item;
import com.jamestiotio.sentienterprize.models.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class NewItemActivity extends BaseActivity {

    private static final String TAG = "NewItemActivity";
    private static final String REQUIRED = "Required";
    private static final String INVALID = "Invalid";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mNameField;
    private EditText mBodyField;
    private EditText mAmountField;
    private EditText mUnitPriceField;
    private FloatingActionButton mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mNameField = findViewById(R.id.fieldName);
        mBodyField = findViewById(R.id.fieldBody);
        mAmountField = findViewById(R.id.fieldAmount);
        mUnitPriceField = findViewById(R.id.fieldUnitPrice);
        mSubmitButton = findViewById(R.id.fabSubmitItem);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitItem();
            }
        });
    }

    private void submitItem() {
        final String name = mNameField.getText().toString();
        final String body = mBodyField.getText().toString();
        final Integer amount;
        final BigDecimal unitPrice;

        try {
            amount = Integer.valueOf(mAmountField.getText().toString());
        }
        catch (NumberFormatException ex) {
            mAmountField.setError(INVALID);
            return;
        }

        try {
            unitPrice = new BigDecimal(mUnitPriceField.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        catch (NumberFormatException ex) {
            mUnitPriceField.setError(INVALID);
            return;
        }

        // Name is required
        if (TextUtils.isEmpty(name)) {
            mNameField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-items
        setEditingEnabled(false);
        Toast.makeText(this, "Adding item...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("RealApparel").child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewItemActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            checkForUniqueItem(userId, user.username, name, body, amount, unitPrice);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        mNameField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.show();
        } else {
            mSubmitButton.hide();
        }
    }

    private void checkForUniqueItem(String userId, String username, String name, String body, Integer amount, BigDecimal unitPrice) {
        mDatabase.child("RealApparel").child("inventory").child("items").child(name).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            // Write new item
                            writeNewItem(userId, username, name, body, amount, unitPrice);
                        } else {
                            // Duplicate item name, error out
                            Log.e(TAG, name + " is a duplicate item name.");
                            Toast.makeText(NewItemActivity.this,
                                    "Error: duplicate item name.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getItemName:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
    }

    // [START write_fan_out]
    private void writeNewItem(String userId, String username, String name, String body, Integer amount, BigDecimal unitPrice) {
        // Create new item at /user-items/$userid/$itemid and at
        // /items/$itemid simultaneously
        String key = name;
        Item item = new Item(userId, username, name, body, amount, unitPrice);
        Map<String, Object> itemValues = item.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/RealApparel/inventory/items/" + key, itemValues);
        childUpdates.put("/RealApparel/inventory/user-items/" + userId + "/" + key, itemValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}
