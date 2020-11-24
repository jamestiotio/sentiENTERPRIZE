package com.jamestiotio.sentienterprize.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyTopItemsFragment extends ItemListFragment {

    public MyTopItemsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_items_query]
        // My top items by number of stars
        String myUserId = getUid();
        Query myTopItemsQuery = databaseReference.child("RealApparel").child("user-items").child(myUserId)
                .orderByChild("starCount");
        // [END my_top_items_query]

        return myTopItemsQuery;
    }
}
