package com.jamestiotio.sentienterprize;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class EditItemActivity extends BaseActivity {

    public static final String EXTRA_ITEM_KEY = "item_key";
    private static final String TAG = "NewItemActivity";
    private static final String REQUIRED = "Required";
    private static final String INVALID = "Invalid";
    private String mItemKey;
    private Item myitem;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mItemReference;
    private ValueEventListener mItemListener;
    // [END declare_database_ref]

    private EditText mNameField;
    private EditText mBodyField;
    private EditText mAmountField;
    private EditText mUnitPriceField;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Get item key from intent
        mItemKey = getIntent().getStringExtra(EXTRA_ITEM_KEY);
        if (mItemKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_ITEM_KEY");
        }

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mItemReference = FirebaseDatabase.getInstance().getReference()
                .child("RealApparel").child("inventory").child("items").child(mItemKey);

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
        } catch (NumberFormatException ex) {
            mAmountField.setError(INVALID);
            return;
        }

        try {
            unitPrice = new BigDecimal(mUnitPriceField.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (NumberFormatException ex) {
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
        Toast.makeText(this, "Updating item...", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(EditItemActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            writeEditedItem(userId, user.username, name, body, amount, unitPrice);
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

    // [START write_fan_out]
    private void writeEditedItem(String userId, String username, String name, String body, Integer amount, BigDecimal unitPrice) {
        // Create new item at /user-items/$userid/$itemid and at
        // /items/$itemid simultaneously
        String key = name;
        myitem.name = name;
        myitem.body = body;
        myitem.amount = amount;
        myitem.unitPrice = unitPrice.doubleValue();
        Map<String, Object> itemValues = myitem.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/RealApparel/inventory/items/" + key, itemValues);
        childUpdates.put("/RealApparel/inventory/user-items/" + userId + "/" + key, itemValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]

    private void setEditingEnabled(boolean enabled) {
        mNameField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setEnabled(true);
        } else {
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the item
        // [START item_value_event_listener]
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Item object and use the values to update the UI
                myitem = dataSnapshot.getValue(Item.class);

                // [START_EXCLUDE]
                mNameField.setText(myitem.name);
                mBodyField.setText(myitem.body);
                mAmountField.setText(String.valueOf(myitem.amount));
                mUnitPriceField.setText(String.valueOf(myitem.unitPrice));
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Item failed, log a message
                Log.w(TAG, "loadItem:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(EditItemActivity.this, "Failed to load item.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mItemReference.addValueEventListener(itemListener);
        // [END item_value_event_listener]

        // Keep copy of item listener so we can remove it when app stops
        mItemListener = itemListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove item value event listener
        if (mItemListener != null) {
            mItemReference.removeEventListener(mItemListener);
        }
    }
}