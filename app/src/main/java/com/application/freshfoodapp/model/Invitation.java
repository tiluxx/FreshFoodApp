package com.application.freshfoodapp.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invitation {
    @SerializedName("ownerId")
    private String ownerId;
    @SerializedName("ownerDisplayName")
    private String ownerDisplayName;
    @SerializedName("ownerEmail")
    private String ownerEmail;
    @SerializedName("ownerPhotoUrl")
    private Uri ownerPhotoUrl;
    @SerializedName("receiverId")
    private String receiverId;
    @SerializedName("sharingPantries")
    private String sharingKitchenId;
    @SerializedName("message")
    private String message;

    private String invitationId;

    public Invitation(String ownerId, String ownerDisplayName, String ownerEmail, Uri ownerPhotoUrl, String receiverId, String sharingKitchenId) {
        this.ownerId = ownerId;
        this.ownerDisplayName = ownerDisplayName;
        this.ownerEmail = ownerEmail;
        this.ownerPhotoUrl = ownerPhotoUrl;
        this.receiverId = receiverId;
        this.sharingKitchenId = sharingKitchenId;
    }

    public Invitation(String ownerId, String ownerDisplayName, String ownerEmail, Uri ownerPhotoUrl, String receiverId, String sharingKitchenId, String message) {
        this.ownerId = ownerId;
        this.ownerDisplayName = ownerDisplayName;
        this.ownerEmail = ownerEmail;
        this.ownerPhotoUrl = ownerPhotoUrl;
        this.receiverId = receiverId;
        this.sharingKitchenId = sharingKitchenId;
        this.message = message;
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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Uri getOwnerPhotoUrl() {
        return ownerPhotoUrl;
    }

    public void setOwnerPhotoUrl(Uri ownerPhotoUrl) {
        this.ownerPhotoUrl = ownerPhotoUrl;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSharingKitchenId() {
        return sharingKitchenId;
    }

    public void setSharingKitchenId(String sharingKitchenId) {
        this.sharingKitchenId = sharingKitchenId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }
}
