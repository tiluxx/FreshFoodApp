package com.application.freshfoodapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restriction {
    @SerializedName("ownerId")
    private String ownerId;
    @SerializedName("restrictions")
    private List<String> restrictions;

    public Restriction() {}

    public Restriction(String ownerId, List<String> restrictions) {
        this.ownerId = ownerId;
        this.restrictions = restrictions;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<String> restrictions) {
        this.restrictions = restrictions;
    }
}
