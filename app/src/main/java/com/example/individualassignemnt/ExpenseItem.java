package com.example.individualassignemnt;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseItem {
    private String name;
    private HashMap<String, Double> titleAmountMap;

    public ExpenseItem(String name, String title, double amount) {
        this.name = name;
        this.titleAmountMap = new HashMap<>();
        addAmount(title, amount);
    }

    public String getName() {
        return name;
    }

    public double getTotalAmount() {
        double totalAmount = 0;
        for (Double amount : titleAmountMap.values()) {
            totalAmount += amount;
        }
        return totalAmount;
    }

    public void addAmount(String title, double amount) {
        if (titleAmountMap.containsKey(title)) {
            double currentAmount = titleAmountMap.get(title);
            titleAmountMap.put(title, currentAmount + amount);
        } else {
            titleAmountMap.put(title, amount);
        }
    }

}