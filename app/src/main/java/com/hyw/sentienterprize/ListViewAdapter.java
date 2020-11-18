package com.hyw.sentienterprize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.List;
import static com.hyw.sentienterprize.TestData.formatPrice;

public class ListViewAdapter extends BaseExpandableListAdapter {

    private List<String> lstGroups;
    private HashMap<String, List<Item>> lstItemsGroups;
    private Context context;
    private List<Double> subTotalValues;

    // Constructor 1
    public ListViewAdapter(Context context, TestData testData){
        this.context = context;
        lstGroups = testData.getTransCode();
        lstItemsGroups = testData.getTransList();
        subTotalValues = testData.getSubTotalValues();
    }

    // Constructor 2
    public ListViewAdapter(Context context, List<String> groups, HashMap<String, List<Item>> itemsGroups) {
        // initialize class variables
        this.context = context;
        lstGroups = groups;
        lstItemsGroups = itemsGroups;
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

        groupTitle.setText((String)"Transaction code: " + getGroup(groupPosition));
        count.setText(String.valueOf(getChildrenCount(groupPosition)));
        subTotal.setText(formatPrice(subTotalValues.get(groupPosition)));

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