package com.application.freshfoodapp.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Kitchen {
    @SerializedName("ownerId")
    private String ownerId;
    @SerializedName("ownerDisplayName")
    private String ownerDisplayName;
    @SerializedName("ownerPhotoUrl")
    private Uri ownerPhotoUrl;
    @SerializedName("subOwnerIds")
    private List<String> subOwnerIds;
    private String kitchenId;

    public Kitchen(String ownerId) {
        this.ownerId = ownerId;
    }

    public Kitchen(String ownerId, String ownerDisplayName, Uri ownerPhotoUrl) {
        this.ownerId = ownerId;
        this.ownerDisplayName = ownerDisplayName;
        this.ownerPhotoUrl = ownerPhotoUrl;
    }

    public Kitchen(String ownerId, String ownerDisplayName, Uri ownerPhotoUrl, List<String> subOwnerIds) {
        this.ownerId = ownerId;
        this.ownerDisplayName = ownerDisplayName;
        this.ownerPhotoUrl = ownerPhotoUrl;
        this.subOwnerIds = subOwnerIds;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }

    public void setOwnerDisplayName(String ownerDisplayName) {
        this.ownerDisplayName = ownerDisplayName;
    }

    public Uri getOwnerPhotoUrl() {
        return ownerPhotoUrl;
    }

    public void setOwnerPhotoUrl(Uri ownerPhotoUrl) {
        this.ownerPhotoUrl = ownerPhotoUrl;
    }

    public List<String> getSubOwnerIds() {
        return subOwnerIds;
    }

    public void setSubOwnerIds(List<String> subOwnerIds) {
        this.subOwnerIds = subOwnerIds;
    }

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId;
    }
}
