package com.jamestiotio.sentienterprize.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyItemsFragment extends ItemListFragment {

    public MyItemsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my items
        return databaseReference.child("RealApparel").child("inventory").child("user-items")
                .child(getUid());
    }
}
