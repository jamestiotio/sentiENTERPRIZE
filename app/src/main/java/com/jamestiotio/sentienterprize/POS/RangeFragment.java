package com.jamestiotio.sentienterprize.POS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import com.jamestiotio.sentienterprize.R;

public class RangeFragment extends Fragment {
    ArrayList<TransactionSingle> fullData;
    ExpandableListView transactions;
    TextView fragTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_range, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // find widgets by items
        transactions = view.findViewById(R.id.elvItems);
        fragTotal = getActivity().findViewById(R.id.TotalSales);

        if (getArguments() != null){
            fullData = (ArrayList<TransactionSingle>) getArguments().getSerializable("data");
        }

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        // Set default view
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = Calendar.getInstance().getTime();
        String todayDateStr = sdf.format(todayDate.getTime());
        ArrayList<TransactionSingle> today = getSpecificDay(fullData, todayDateStr);
        fragTotal.setText(getfragTotal(today));

        // Create an adapter (BaseExpandableListAdapter) with the data above
        ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), today);

        // defines the ExpandableListView adapter
        transactions.setAdapter(listViewAdapter);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateSelected = sdf.format(date.getTime());
                ArrayList<TransactionSingle> specificDay = getSpecificDay(fullData, dateSelected);

                // Create an adapter (BaseExpandableListAdapter) with the data above
                ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), specificDay);
                // defines the ExpandableListView adapter
                transactions.setAdapter(listViewAdapter);

                if (specificDay.size() == 0){
                    Toast.makeText(getActivity(), "No transaction on this day.", Toast.LENGTH_SHORT).show();
                    fragTotal.setText("0.00");
                }else{
                    fragTotal.setText(getfragTotal(specificDay));
                }

            }
        });
    }

    public ArrayList<TransactionSingle> getSpecificDay(ArrayList<TransactionSingle> transList, String date){
        ArrayList<TransactionSingle> specificDay = new ArrayList<>();

        for (TransactionSingle t : transList){
            String currentDate = t.getDatetime().substring(0,10);
            if (date.equals(currentDate)){
                specificDay.add(t);
            }
        }

        return specificDay;
    }

    public String getfragTotal(ArrayList<TransactionSingle> l){
        double total = 0;
        for (TransactionSingle t : l){
            total += t.getTransTotal();
        }
        return String.format("%,.2f", total);

    }
}