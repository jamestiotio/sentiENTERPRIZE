package com.jamestiotio.sentienterprize.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// [START item_class]
@IgnoreExtraProperties
public class Item {

    public String uid;
    public String author;
    public String title;
    public String body;
    public Integer amount;
    public Double unitPrice;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(String uid, String author, String title, String body, Integer amount, BigDecimal unitPrice) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.amount = amount;
        this.unitPrice = unitPrice.doubleValue(); // Convert BigDecimal to Double since Firebase does not support serialization of BigDecimal objects
    }

    // [START item_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("amount", amount);
        result.put("unitPrice", unitPrice);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END item_to_map]

}
// [END item_class]
