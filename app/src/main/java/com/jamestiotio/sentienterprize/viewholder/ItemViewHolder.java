package com.jamestiotio.sentienterprize.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jamestiotio.sentienterprize.R;
import com.jamestiotio.sentienterprize.models.Item;
import com.jamestiotio.sentienterprize.utils.CheckNetworkAvailability;
import com.jamestiotio.sentienterprize.utils.DownloadUserProfilePhoto;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public View itemView;
    public TextView nameView;
    public TextView authorView;
    public ImageView authorPhotoView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView amountView;
    public TextView unitPriceView;

    public ItemViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        nameView = itemView.findViewById(R.id.itemName);
        authorView = itemView.findViewById(R.id.itemAuthor);
        authorPhotoView = itemView.findViewById(R.id.itemAuthorPhoto);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.itemNumStars);
        bodyView = itemView.findViewById(R.id.itemBody);
        amountView = itemView.findViewById(R.id.itemAmount);
        unitPriceView = itemView.findViewById(R.id.itemUnitPrice);
    }

    public void bindToItem(Item item, View.OnClickListener starClickListener) {
        // If there is a network connection available, get user email and download user's profile photo from Gravatar
        if (CheckNetworkAvailability.isNetworkAvailable(this.itemView.getContext())) {
            String email;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                email = user.getEmail();
            }
            else {
                email = "";
            }
            DownloadUserProfilePhoto downloadUserProfilePhoto = new DownloadUserProfilePhoto(this.itemView.getContext(), authorPhotoView);
            downloadUserProfilePhoto.execute(email);
        }

        nameView.setText(item.name);
        authorView.setText(item.author);
        numStarsView.setText(String.valueOf(item.starCount));
        bodyView.setText(item.body);
        amountView.setText(String.valueOf(item.amount));
        unitPriceView.setText(String.valueOf(item.unitPrice));

        starView.setOnClickListener(starClickListener);
    }
}
