package com.example.posystem2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WeekData implements Serializable {
    private ArrayList<DayData> dayDataArrayList;
    private double Total;
    private HashMap<String, ArrayList<Transaction>> dayMap;

    public WeekData(int n){
        generateWeekData(n);
    }

    public void generateWeekData(int n_of_days){
        dayMap = new HashMap<>();
        dayDataArrayList = new ArrayList<>();

        for (int i = 0; i < n_of_days; i++){
            int n_of_trans = new Random().nextInt(5) + 1;

            DayData dayData = new DayData(n_of_trans);

            Total += dayData.getDayTotal();
            dayDataArrayList.add(dayData);
            dayMap.put(dayData.getDate(), dayData.getTransList());
        }

        Collections.sort(dayDataArrayList);
        Collections.reverse(dayDataArrayList);
    }

    public ArrayList<DayData> getDayDataArrayList() {
        return dayDataArrayList;
    }

    public double getTotal() {
        return Total;
    }

    public HashMap<String, ArrayList<Transaction>> getDayMap() {
        return dayMap;
    }

    public DayData getLatestDay(){
        return getDayDataArrayList().get(0);
    }

    @Override
    public String toString() {
        return "WeekData{" +
                "dayDataArrayList=" + dayDataArrayList +
                '}';
    }
}
