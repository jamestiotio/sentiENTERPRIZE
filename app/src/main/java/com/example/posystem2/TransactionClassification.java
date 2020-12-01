package com.example.posystem2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TransactionClassification {
    private ArrayList<TransactionSingle> dayList = new ArrayList<>();;
    private ArrayList<TransactionSingle> weekList = new ArrayList<>();
    private ArrayList<TransactionSingle> monthList = new ArrayList<>();;

    public TransactionClassification(ArrayList<TransactionSingle> fullData){
        getDayList(fullData);
        getWeekList(fullData);
        getMonthList(fullData);

    }

    public ArrayList<TransactionSingle> getDayList() {
        return dayList;
    }

    public ArrayList<TransactionSingle> getWeekList() {
        return weekList;
    }

    public ArrayList<TransactionSingle> getMonthList() {
        return monthList;
    }

    public void getDayList(ArrayList<TransactionSingle> fullData) {
        String latestDate = "";

        // get largest timestamp
        Long temp = Long.valueOf(0);
        for (TransactionSingle t : fullData) {
            if (t.getTimestamp() > temp) {
                temp = t.getTimestamp();
                latestDate = t.getDatetime().substring(0,10);
            }
        }

        // get all latest day's transaction
        for (TransactionSingle t : fullData){
            String tDateTime = t.getDatetime().substring(0,10);

            if (tDateTime.equals(latestDate)){
                dayList.add(t);
            }
        }

        Collections.sort(dayList);
    }

    public void getWeekList(ArrayList<TransactionSingle> fullData) {
        String latestDate = "";

        // get largest timestamp
        Long temp = Long.valueOf(0);
        for (TransactionSingle t : fullData) {
            if (t.getTimestamp() > temp) {
                temp = t.getTimestamp();
                latestDate = t.getDatetime().substring(0,10);
            }
        }

        // get last 7 days
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(temp);

        String[] days = new String[7];
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        days[0] = sdf.format(date);

        for(int i = 1; i < 7; i++){
            cal.add(Calendar.DAY_OF_YEAR, -1);
            date = cal.getTime();
            System.out.println(date);
            days[i] = sdf.format(date);
        }

        List<String> list = Arrays.asList(days);

        // get all transactions in the last 7 days
        for (TransactionSingle t : fullData){
            String tDateTime = t.getDatetime().substring(0,10);
            System.out.println("XXX");
            System.out.println(tDateTime);
            System.out.println(list);
            if (list.contains(tDateTime)){
                weekList.add(t);
                System.out.println("Gay");
                System.out.println("XXX");
            }
        }

        Collections.sort(weekList);
    }

    public void getMonthList(ArrayList<TransactionSingle> fullData) {
        String latestDate = "";

        // get largest timestamp
        Long temp = Long.valueOf(0);
        for (TransactionSingle t : fullData) {
            if (t.getTimestamp() > temp) {
                temp = t.getTimestamp();
                latestDate = t.getDatetime().substring(0,10);
            }
        }

        // get last 7 days
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(temp);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String[] days = new String[30];
        days[0] = sdf.format(date);

        for(int i = 1; i < 30; i++){
            cal.add(Calendar.DAY_OF_YEAR, -1);
            date = cal.getTime();
            days[i] = sdf.format(date);
        }

        List<String> list = Arrays.asList(days);

        // get all transactions in the last 7 days
        for (TransactionSingle t : fullData){
            String tDateTime = t.getDatetime().substring(0,10);

            if (list.contains(tDateTime)){
                monthList.add(t);
            }
        }

        Collections.sort(monthList);
    }

}
