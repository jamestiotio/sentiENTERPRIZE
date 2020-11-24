package com.jamestiotio.sentienterprize.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamestiotio.sentienterprize.R;
import com.jamestiotio.sentienterprize.models.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView amountView;
    public TextView unitPriceView;

    public ItemViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.itemTitle);
        authorView = itemView.findViewById(R.id.itemAuthor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.itemNumStars);
        bodyView = itemView.findViewById(R.id.itemBody);
        amountView = itemView.findViewById(R.id.itemAmount);
        unitPriceView = itemView.findViewById(R.id.itemUnitPrice);
    }

    public void bindToItem(Item item, View.OnClickListener starClickListener) {
        titleView.setText(item.title);
        authorView.setText(item.author);
        numStarsView.setText(String.valueOf(item.starCount));
        bodyView.setText(item.body);
        amountView.setText(String.valueOf(item.amount));
        unitPriceView.setText(String.valueOf(item.unitPrice));

        starView.setOnClickListener(starClickListener);
    }
}
