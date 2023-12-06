package com.application.freshfoodapp.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Product {
    @SerializedName("success")
    private boolean status;
    @SerializedName("barcode")
    private String barcode;
    @SerializedName("title")
    private String title;
    @SerializedName("alias")
    private String shortTitle;
    @SerializedName("description")
    private String description;
    @SerializedName("brand")
    private String brand;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("category")
    private String category;
    @SerializedName("categories")
    private String categories;
    @SerializedName("images")
    private List<String> images = null;
    @SerializedName("metadata")
    private BarcodeMetadata metadata = null;
    @SerializedName("metanutrition")
    private MetaNutrition metaNutrition;

    private long expiryDate;
    private String kitchenId;
    private String productId;
    private List<String> productCategorizes = null;
    private String pantry;

    public Product() {}

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<String> getProductCategorizes() {
        return productCategorizes;
    }

    public void setProductCategorizes(List<String> productCategorizes) {
        this.productCategorizes = productCategorizes;
    }

    public String getPantry() {
        return pantry;
    }

    public void setPantry(String pantry) {
        this.pantry = pantry;
    }

    public boolean isSuccess() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public BarcodeMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BarcodeMetadata metadata) {
        this.metadata = metadata;
    }

    public MetaNutrition getMetaNutrition() {
        return metaNutrition;
    }

    public void setMetaNutrition(MetaNutrition metaNutrition) {
        this.metaNutrition = metaNutrition;
    }
}
