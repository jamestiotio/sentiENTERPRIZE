package com.example.posystem2;

public class DataEvent {
    public DayData dayData;
    public String message;

    public DataEvent(DayData dayData){
        this.dayData = dayData;
    }

    public DataEvent(String message){
        this.message = message;
    }
}
