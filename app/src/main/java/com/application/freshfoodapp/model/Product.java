package com.application.freshfoodapp.model;

import java.time.LocalDateTime;

public class Product {
    private String name;
    private LocalDateTime  expiryDate;
    private int quantity;
    private int weight;
    private String unitAmount;

    public Product(String name, LocalDateTime expiryDate, int quantity, int weight, String unitAmount) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.weight = weight;
        this.unitAmount = unitAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime  getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime  expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(String unitAmount) {
        this.unitAmount = unitAmount;
    }
}
