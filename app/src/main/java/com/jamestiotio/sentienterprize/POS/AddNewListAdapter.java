package com.jamestiotio.sentienterprize.POS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jamestiotio.sentienterprize.R;

import java.util.ArrayList;

public class AddNewListAdapter extends ArrayAdapter<Item> {
    ArrayList<Item> l;


    public AddNewListAdapter(Context context, ArrayList<Item> l) {
        super(context, 0, l);
        this.l = l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item i = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_group, parent, false);
        }

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.itemPrice);
        TextView itemQuantity = (TextView) convertView.findViewById(R.id.itemQuantity);

        itemName.setText(i.getName());
        itemPrice.setText(i.getStringAmount());
        itemQuantity.setText(""+i.getQuantity());

        // Return the completed view to render on screen
        return convertView;
    }
}
