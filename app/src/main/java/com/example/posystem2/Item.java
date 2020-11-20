package com.example.posystem2;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private int quantity;
    private double unitPrice;

    public Item(String name, int quantity, double unitPrice){
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getStringAmount(){
        return String.format("%,.2f", getUnitPrice());
    }
}
