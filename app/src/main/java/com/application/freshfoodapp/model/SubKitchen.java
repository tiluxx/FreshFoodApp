package com.application.freshfoodapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubKitchen {
    private String subOwnerId;
    private String subOwnerDisplayName;

    public SubKitchen() {
    }

    public SubKitchen(String subOwnerId, String subOwnerDisplayName) {
        this.subOwnerId = subOwnerId;
        this.subOwnerDisplayName = subOwnerDisplayName;
    }

    public String getSubOwnerId() {
        return subOwnerId;
    }

    public void setSubOwnerId(String subOwnerId) {
        this.subOwnerId = subOwnerId;
    }

    public String getSubOwnerDisplayName() {
        return subOwnerDisplayName;
    }

    public void setSubOwnerDisplayName(String subOwnerDisplayName) {
        this.subOwnerDisplayName = subOwnerDisplayName;
    }
}
