package com.example.posystem2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewAdapter extends BaseExpandableListAdapter {
    private List<String> lstGroups;
    private List<String> timeList;
    private List<String> imageList;
    private List<String> dateList;
    private HashMap<String, List<Item>> lstItemsGroups;
    private Context context;
    private List<Double> subTotalValues;

    public ListViewAdapter(Context context, DayData dayData){
        this.context = context;
        this.lstItemsGroups = dayData.getTransMap();

        lstGroups = new ArrayList<>();
        subTotalValues = new ArrayList<>();
        timeList = new ArrayList<>();
        imageList = new ArrayList<>();
        dateList = new ArrayList<>();

        dateList.add(dayData.getDate());

        for (Transaction t : dayData.getTransList()){
            lstGroups.add(t.getCode());
            subTotalValues.add(t.getTransTotal());
            timeList.add(t.getTime());
            imageList.add(t.getTransType());
        }
    }

    public ListViewAdapter(Context context, WeekData weekData, int nOfDays){
        this.context = context;

        lstItemsGroups = new HashMap<>();
        lstGroups = new ArrayList<>();
        subTotalValues = new ArrayList<>();
        timeList = new ArrayList<>();
        imageList = new ArrayList<>();
        dateList = new ArrayList<>();

        int count = 0;
        while (count < nOfDays && count < weekData.getDayDataArrayList().size()){
            DayData d = weekData.getDayDataArrayList().get(count);
            lstItemsGroups.putAll(d.getTransMap());
            for (Transaction t : d.getTransList()){
                lstGroups.add(t.getCode());
                subTotalValues.add(t.getTransTotal());
                timeList.add(t.getTime());
                imageList.add(t.getTransType());
                dateList.add(d.getDate());
            }
            count++;
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
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        ImageView imageGroup = (ImageView) convertView.findViewById(R.id.imageGroup);

        groupTitle.setText((String)"Transaction code: " + getGroup(groupPosition));
        count.setText(String.valueOf(getChildrenCount(groupPosition)));
        subTotal.setText(String.format("%,.2f", subTotalValues.get(groupPosition)));
        time.setText(timeList.get(groupPosition));

        if (imageList.get(groupPosition) == "card"){
            imageGroup.setImageResource(R.drawable.creditcard);
        }else{
            imageGroup.setImageResource(R.drawable.money);
        }

        if (dateList.size() == 1){
            date.setText(dateList.get(0));
        }else{
            date.setText(dateList.get(groupPosition));
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