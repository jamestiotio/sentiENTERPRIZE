package com.jamestiotio.sentienterprize;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamestiotio.sentienterprize.models.Comment;
import com.jamestiotio.sentienterprize.models.Item;
import com.jamestiotio.sentienterprize.models.User;
import com.jamestiotio.sentienterprize.utils.CheckNetworkAvailability;
import com.jamestiotio.sentienterprize.utils.DownloadUserProfilePhoto;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ItemDetailActivity";

    public static final String EXTRA_ITEM_KEY = "item_key";

    private DatabaseReference mItemReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mItemListener;
    private String mItemKey;
    private CommentAdapter mAdapter;

    private TextView mAuthorView;
    private ImageView mAuthorPhotoView;
    private TextView mNameView;
    private TextView mBodyView;
    private TextView mAmountView;
    private TextView mUnitPriceView;
    private EditText mCommentField;
    private MaterialButton mCommentButton;
    private RecyclerView mCommentsRecycler;
    private Button editButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Get item key from intent
        mItemKey = getIntent().getStringExtra(EXTRA_ITEM_KEY);
        if (mItemKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_ITEM_KEY");
        }

        // Initialize Database
        mItemReference = FirebaseDatabase.getInstance().getReference()
                .child("RealApparel").child("inventory").child("items").child(mItemKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("RealApparel").child("inventory").child("item-comments").child(mItemKey);

        // Initialize Views
        mAuthorView = findViewById(R.id.itemAuthor);
        mAuthorPhotoView = findViewById(R.id.itemAuthorPhoto);
        mNameView = findViewById(R.id.itemName);
        mBodyView = findViewById(R.id.itemBody);
        mAmountView = findViewById(R.id.itemAmount);
        mUnitPriceView = findViewById(R.id.itemUnitPrice);
        mCommentField = findViewById(R.id.fieldCommentText);
        mCommentButton = findViewById(R.id.buttonItemComment);
        mCommentsRecycler = findViewById(R.id.recyclerItemComments);

        editButton = findViewById(R.id.buttonEdit);
        deleteButton = findViewById(R.id.buttonDelete);

        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
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
                Item item = dataSnapshot.getValue(Item.class);

                // If there is a network connection available, get user email and download user's profile photo from Gravatar
                if (CheckNetworkAvailability.isNetworkAvailable(ItemDetailActivity.this)) {
                    String email;
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        email = user.getEmail();
                    }
                    else {
                        email = "";
                    }
                    DownloadUserProfilePhoto downloadUserProfilePhoto = new DownloadUserProfilePhoto(ItemDetailActivity.this, mAuthorPhotoView);
                    downloadUserProfilePhoto.execute(email);
                }

                // [START_EXCLUDE]
                mAuthorView.setText(item.author);
                mNameView.setText(item.name);
                mBodyView.setText(item.body);
                mAmountView.setText(String.valueOf(item.amount));
                mUnitPriceView.setText(String.valueOf(item.unitPrice));
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Item failed, log a message
                Log.w(TAG, "loadItem:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(ItemDetailActivity.this, "Failed to load item.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mItemReference.addValueEventListener(itemListener);
        // [END item_value_event_listener]

        // Keep copy of item listener so we can remove it when app stops
        mItemListener = itemListener;

        // Listen for comments
        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove item value event listener
        if (mItemListener != null) {
            mItemReference.removeEventListener(mItemListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonItemComment) {
            itemComment();
        }
        else if (i == R.id.buttonEdit) {
            itemEdit();
        }
        else if (i == R.id.buttonDelete) {
            itemDelete();
        }
    }

    private void itemEdit() {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(ItemDetailActivity.EXTRA_ITEM_KEY, mItemKey);
        startActivity(intent);
    }

    private void itemDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Do you really want to delete this item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mItemReference.removeValue();
                        Toast.makeText(ItemDetailActivity.this, "Item deleted. Returning to main inventory page...",
                                Toast.LENGTH_LONG).show();
                        Intent intentGoToInventory = new Intent(ItemDetailActivity.this, InventoryActivity.class);
                        startActivity(intentGoToInventory);
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void itemComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("RealApparel").child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.username;

                        // Create new comment object
                        String commentText = mCommentField.getText().toString();
                        Comment comment = new Comment(uid, authorName, commentText);

                        // Push the comment, it will appear in the list
                        mCommentsReference.push().setValue(comment);

                        // Clear the field
                        mCommentField.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.commentAuthor);
            bodyView = itemView.findViewById(R.id.commentBody);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Replace with the new data
                        mComments.set(commentIndex, newComment);

                        // Update the RecyclerView
                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Remove data from the list
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "itemComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }
}
