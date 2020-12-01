package com.example.posystem2;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    public String branchName;
    public String companyId;
    public String orders;
    public String ordersMonth;
    public String profit;
    public String profitMonth;
    public String transactions;
    public String transactionsMonth;

    public Profile() {
        // Default constructor
    }

    public Profile(String branchName, String companyId, String orders, String ordersMonth, String profit, String profitMonth, String transactions, String transactionsMonth) {
        this.branchName = branchName;
        this.companyId = companyId;
        this.orders = orders;
        this.ordersMonth = ordersMonth;
        this.profit = profit;
        this.profitMonth = profitMonth;
        this.transactions = transactions;
        this.transactionsMonth = transactionsMonth;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("branchName", branchName);
        result.put("companyId", companyId);
        result.put("orders", orders);
        result.put("ordersMonth", ordersMonth);
        result.put("profit", profit);
        result.put("profitMonth", profitMonth);
        result.put("transactions", transactions);
        result.put("transactionsMonth", transactionsMonth);

        return result;
    }
}
