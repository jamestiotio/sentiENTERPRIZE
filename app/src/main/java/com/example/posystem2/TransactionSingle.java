package com.example.posystem2;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

public class TransactionSingle implements Serializable, Comparable<TransactionSingle> {
    private String transType;
    private String code;
    private String datetime;
    private double discountRate;
    private Long timestamp;
    private double transTotal;
    private ArrayList<Item> itemList;

    public TransactionSingle(DataSnapshot snapshot){
        itemList = new ArrayList<>();

        this.code = snapshot.getKey();

        if ((int)(Math.random()*60) < 30){
            transType = "card";
        }else{
            transType = "cash";
        }

        this.discountRate = Double.parseDouble(snapshot.child("discountRate").getValue().toString());
        this.transTotal = Double.parseDouble(snapshot.child("finalPrice").getValue().toString());
        this.timestamp = Long.parseLong(snapshot.child("timestamp").getValue().toString());

        this.datetime = setDateTime(timestamp);

        for (DataSnapshot item : snapshot.child("items").getChildren()){
            processItem(item);
        }
    }

    public void processItem(DataSnapshot snapshot){
        String itemName = snapshot.getKey();
        int quantity = Integer.parseInt(snapshot.child("quantity").getValue().toString());
        double unitPrice = (double) snapshot.child("unitPrice").getValue();
        Item item = new Item(itemName, quantity, unitPrice);
        itemList.add(item);
    }

    public String setDateTime(Long timestamp){
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        return formatted;
    }

    @Override
    public int compareTo(TransactionSingle o) {
        // reverse ordering
        if (this.timestamp - o.timestamp > 0){
            return -1;
        }else if (this.timestamp - o.timestamp == 0) {
            return 0;
        }else{
            return 1;
        }
    }

    public String getTransType() {
        return transType;
    }

    public String getCode() {
        return code;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getDatetime() {
        return datetime;
    }

    public double getTransTotal() {
        return transTotal;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public String toString() {
        return getDatetime();
    }
}
