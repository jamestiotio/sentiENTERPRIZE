package com.jamestiotio.sentienterprize.POS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamestiotio.sentienterprize.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewAdapter extends BaseExpandableListAdapter {
    private List<String> lstGroups;
    private List<String> datetimeList;
    private List<String> imageList;
    private HashMap<String, List<Item>> lstItemsGroups;
    private Context context;
    private List<Double> subTotalValues;

    public ListViewAdapter(Context context, ArrayList<TransactionSingle> transList) {
        this.context = context;

        // initialise attributes
        lstItemsGroups = new HashMap<>();
        lstGroups = new ArrayList<>();
        subTotalValues = new ArrayList<>();
        datetimeList = new ArrayList<>();
        imageList = new ArrayList<>();

        // get attributes
        for (TransactionSingle t : transList) {
            lstGroups.add(t.getCode());
            subTotalValues.add(t.getTransTotal());
            datetimeList.add(t.getDatetime());
            imageList.add(t.getTransType());

            lstItemsGroups.put(t.getCode(), t.getItemList());
        }
    }

    @Override
    public int getGroupCount() {
        // returns groups count
        return lstGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // returns items count of a group
        return lstItemsGroups.get(getGroup(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // returns a group
        return lstGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // returns a group item
        return lstItemsGroups.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // return the group id
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // returns the item id of group
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // returns if the ids are specific ( unique for each group or item)
        // or relatives
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // create main items (groups)
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView groupTitle = (TextView) convertView.findViewById(R.id.groupTitle);
        TextView count = (TextView) convertView.findViewById(R.id.itemCount);
        TextView subTotal = (TextView) convertView.findViewById(R.id.subTotal);
        TextView datetime = (TextView) convertView.findViewById(R.id.datetime);
        ImageView imageGroup = (ImageView) convertView.findViewById(R.id.imageGroup);

        groupTitle.setText((String)"Transaction code: " + getGroup(groupPosition));
        count.setText(String.valueOf(getChildrenCount(groupPosition)));
        subTotal.setText(String.format("%,.2f", subTotalValues.get(groupPosition)));
        datetime.setText(datetimeList.get(groupPosition));

        if (imageList.get(groupPosition).equals("Card")){
            imageGroup.setImageResource(R.drawable.creditcard);
        }else{
            imageGroup.setImageResource(R.drawable.money);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // create the subitems (items of groups)
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_group, null);
        }

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.itemPrice);
        TextView itemQuantity = (TextView) convertView.findViewById(R.id.itemQuantity);

        Item item = (Item) getChild(groupPosition, childPosition);
        itemQuantity.setText(Integer.toString(item.getQuantity()) + " ×");
        itemName.setText(item.getName());
        itemPrice.setText(item.getStringAmount());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // returns if the subitem (item of group) can be selected
        return true;
    }
}