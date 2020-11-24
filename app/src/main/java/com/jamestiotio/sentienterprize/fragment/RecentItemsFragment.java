package com.jamestiotio.sentienterprize.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentItemsFragment extends ItemListFragment {

    public RecentItemsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_items_query]
        // Last 100 items, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentItemsQuery = databaseReference.child("RealApparel").child("inventory").child("items")
                .limitToFirst(100);
        // [END recent_items_query]

        return recentItemsQuery;
    }
}
