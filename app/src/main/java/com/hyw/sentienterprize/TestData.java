package com.hyw.sentienterprize;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestData {

    // Need to remake in order to accommodate more data (weeks and months)
    // JSON input?

    private List<String> transCode;
    private ArrayList<List<Item>> dataSet;
    private HashMap<String, List<Item>> transList;
    private double totalPrice;
    private List<Double> subTotalValues;

    public TestData(){
        transCode = Arrays.asList(new String[]{"JF01D","HJ99X","KN72S","BH22D","OQ09W"});
        transList = genTransList();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<Double> getSubTotalValues() {
        return subTotalValues;
    }

    public List<String> getTransCode() {
        return transCode;
    }

    public HashMap<String, List<Item>> getTransList() {
        return transList;
    }

    public HashMap<String, List<Item>> genTransList(){
        // Entry 1
        List<Item> trans1 = new ArrayList<>();
        trans1.add(new Item("Khaki Joggers", 1, 29.95));
        trans1.add(new Item("Pendant necklace", 2, 9.95));
        trans1.add(new Item("Viscose shirt", 1, 19.95));
        trans1.add(new Item("Ankle socks", 3, 29.95));
        trans1.add(new Item("Heavy cotton hoodie", 2, 39.95));

        List<Item> trans2 = new ArrayList<>();
        trans2.add(new Item("Flannel pyjamas", 1, 49.95));
        trans2.add(new Item("Knitted jumper", 1, 59.95));

        List<Item> trans3 = new ArrayList<>();
        trans3.add(new Item("Knitted pyjamas", 1, 49.95));
        trans3.add(new Item("Printed hoodie", 2, 59.95));
        trans3.add(new Item("Viscose shirt", 1, 19.95));
        trans3.add(new Item("Ankle socks", 3, 29.95));

        List<Item> trans4 = new ArrayList<>();
        trans4.add(new Item("Heavy cotton hoodie", 2, 39.95));
        trans4.add(new Item("Flannel pyjamas", 1, 49.95));
        trans4.add(new Item("Knitted jumper", 3, 59.95));

        List<Item> trans5 = new ArrayList<>();
        trans5.add(new Item("Knitted pyjamas", 1, 49.95));
        trans5.add(new Item("Printed hoodie", 2, 59.95));
        trans5.add(new Item("Viscose shirt", 1, 19.95));
        trans5.add(new Item("Ankle socks", 3, 29.95));

        dataSet = new ArrayList<>();
        dataSet.add(trans1);
        dataSet.add(trans2);
        dataSet.add(trans3);
        dataSet.add(trans4);
        dataSet.add(trans5);

        subTotalValues = new ArrayList<>();

        HashMap<String, List<Item>> transList = new HashMap<>();

        for (int i = 0; i < dataSet.size(); i++){
            transList.put(getTransCode().get(i), dataSet.get(i));
            double subTotalValue = 0;
            for (Item j : dataSet.get(i)){
                subTotalValue += j.getUnitPrice() * j.getQuantity();
            }
            subTotalValues.add(subTotalValue);
        }

        for (Double i : subTotalValues){
            totalPrice += i;
        }

        return transList;
    }

    public static String formatPrice(double p){
        return String.format("%,.2f", p);
    }
}
