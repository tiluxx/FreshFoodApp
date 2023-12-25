package com.application.freshfoodapp.model;

public class ItemOfMeal {
    private String urlDish;
    private String labelDish;
    private String numOfServings;
    private String image;

    public ItemOfMeal() {
    }

    public ItemOfMeal(String urlDish, String labelDish, String numOfServings, String image) {
        this.urlDish = urlDish;
        this.labelDish = labelDish;
        this.numOfServings = numOfServings;
        this.image = image;
    }

    public String getUrlDish() {
        return urlDish;
    }

    public void setUrlDish(String urlDish) {
        this.urlDish = urlDish;
    }

    public String getLabelDish() {
        return labelDish;
    }

    public void setLabelDish(String labelDish) {
        this.labelDish = labelDish;
    }

    public String getNumOfServings() {
        return numOfServings;
    }

    public void setNumOfServings(String numOfServings) {
        this.numOfServings = numOfServings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
