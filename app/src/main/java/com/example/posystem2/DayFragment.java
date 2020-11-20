package com.example.posystem2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.widget.ExpandableListView;

public class DayFragment extends Fragment {
    DayData dayData;
    ExpandableListView transactions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_day, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transactions = view.findViewById(R.id.elvItems);

        // Create fake data OR receive fake data
        dayData = new DayData(10);
        if (getArguments() != null){
            dayData = (DayData) getArguments().getSerializable("data");
        }

        // Create an adapter (BaseExpandableListAdapter) with the data above
        ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), dayData);
        // defines the ExpandableListView adapter
        transactions.setAdapter(listViewAdapter);
    }
}