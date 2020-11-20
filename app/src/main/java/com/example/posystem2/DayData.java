package com.example.posystem2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DayData implements Serializable, Comparable<DayData> {
    private String date;
    private double dayTotal;
    private ArrayList<Transaction> transList;
    private HashMap<String, List<Item>> transMap;

    public DayData(int n_of_transactions){
        int day = new Random().nextInt(9) + 10;
        date = "Nov" + " " + day;
        generateDayData(n_of_transactions);
    }

    public void generateDayData(int n_of_transactions){
        transMap = new HashMap<>();
        transList = new ArrayList<>();

        for (int i = 0; i < n_of_transactions; i++){
            int n_of_items = new Random().nextInt(7) + 1;
            Transaction trans = new Transaction(n_of_items);

            dayTotal += trans.getTransTotal();
            transList.add(trans);
            transMap.put(trans.getCode(), trans.getItemList());
        }

        Collections.sort(transList);
        Collections.reverse(transList);
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Transaction> getTransList() {
        return transList;
    }

    public HashMap<String, List<Item>> getTransMap() {
        return transMap;
    }

    public double getDayTotal() {
        return dayTotal;
    }

    public double getDateDouble(){
        // convert date to double for comparison
        double date = 0;
        String[] mthList = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (int i = 0; i < mthList.length; i++){
            String currentMth = this.getDate().substring(0,3);
            if (currentMth.equals(mthList[i])){
                date = (i+1)*100;
            }
        }
        String currentDay = getDate().substring(getDate().length() - 2);
        date += Double.parseDouble(currentDay);

        return date;
    }

    @Override
    public int compareTo(DayData o) {
        if (this.getDateDouble() > o.getDateDouble()){
            return 1;
        }else if (this.getDateDouble() == o.getDateDouble()){
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return "DayData{" +
                "date='" + date + '\'' +
                ", dayTotal=" + dayTotal +
                '}';
    }
}
