package com.jamestiotio.sentienterprize.POS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Random;

public class Transaction implements Serializable, Comparable<Transaction> {
    private String transType;
    private String code;
    private String time;
    private double transTotal;
    private ArrayList<Item> itemList;

    public Transaction(int n){
        // Generate time
        int minute = (int)(Math.random()*60);
        int hour = (int) (Math.random()*10) + 10;

        if (minute < 30){
            transType = "card";
        }else{
            transType = "cash";
        }

        if (minute < 10){
            time = "" + hour + ":" + "0" + minute;
        }else{
            time = "" + hour + ":" + minute;
        }

        // Generate code
        String uuid = UUID.randomUUID().toString();
        code = uuid.substring(0,5);

        generateItem(n);
    }

    public void generateItem(int n){
        String[] l_item = {"Khaki joggers", "Pendant necklace", "Viscose shirt", "Ankle socks",
                "Heavy cotton hoodie", "Flannel pyjamas","Knitted jumper",
                "Wide-leg trousers", "Velvet dress", "Plush jersey dress", "Soft-touch top"};
        double[] l_price = {29.95, 9.95, 24.95, 9.95, 49.95, 39.95, 59.95, 39.95, 34.95, 29.95, 19.95};

        itemList = new ArrayList<>();

        for (int i = 0; i < n; i++){

            int sel = new Random().nextInt(l_item.length);
            int quantity = new Random().nextInt(5) + 1;

            itemList.add(new Item(l_item[sel], quantity, l_price[sel]));
            transTotal += l_price[sel]*quantity;
        }
    }

    public String getCode() {
        return code;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public double getTransTotal() {
        return transTotal;
    }

    public String getTransType() {
        return transType;
    }

    public double getTimeDouble(){
        String t = getTime();
        t = t.replace(":","");
        return Double.parseDouble(t);
    }

    @Override
    public int compareTo(Transaction transaction) {
        if(this.getTimeDouble() > transaction.getTimeDouble()){
            return 1;
        }else if (this.getTimeDouble() == transaction.getTimeDouble()){
            return 0;
        }else{
            return -1;
        }
    }
}
