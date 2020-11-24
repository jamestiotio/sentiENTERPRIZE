package com.jamestiotio.sentienterprize.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.jamestiotio.sentienterprize.R;
import com.jamestiotio.sentienterprize.ItemDetailActivity;
import com.jamestiotio.sentienterprize.models.Item;
import com.jamestiotio.sentienterprize.viewholder.ItemViewHolder;

public abstract class ItemListFragment extends Fragment {

    private static final String TAG = "ItemListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Item, ItemViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public ItemListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_items, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = rootView.findViewById(R.id.messagesList);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query itemsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(itemsQuery, Item.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {

            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new ItemViewHolder(inflater.inflate(R.layout.item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(ItemViewHolder viewHolder, int position, final Item model) {
                final DatabaseReference itemRef = getRef(position);

                // Set click listener for the whole item view
                final String itemKey = itemRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch ItemDetailActivity
                        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                        intent.putExtra(ItemDetailActivity.EXTRA_ITEM_KEY, itemKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this item and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Bind Item to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToItem(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the item is stored
                        DatabaseReference globalItemRef = mDatabase.child("RealApparel").child("inventory").child("items").child(itemRef.getKey());
                        DatabaseReference userItemRef = mDatabase.child("RealApparel").child("inventory").child("user-items").child(model.uid).child(itemRef.getKey());

                        // Run two transactions
                        onStarClicked(globalItemRef);
                        onStarClicked(userItemRef);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    // [START item_stars_transaction]
    private void onStarClicked(DatabaseReference itemRef) {
        itemRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Item p = mutableData.getValue(Item.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the item and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the item and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "itemTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END item_stars_transaction]


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
