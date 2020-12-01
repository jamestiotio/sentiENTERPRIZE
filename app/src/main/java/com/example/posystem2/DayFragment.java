package com.example.posystem2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collections;

public class DayFragment extends Fragment {
    ArrayList<TransactionSingle> requestedData;
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

        if (getArguments() != null){
            requestedData = (ArrayList<TransactionSingle>) getArguments().getSerializable("data");
        }

        // Create an adapter (BaseExpandableListAdapter) with the data above
        ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), requestedData);
        // defines the ExpandableListView adapter
        transactions.setAdapter(listViewAdapter);
    }
}