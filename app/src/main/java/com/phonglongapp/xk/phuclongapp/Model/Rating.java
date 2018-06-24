package com.phonglongapp.xk.phuclongapp.Model;

public class Rating {
    private String id;
    private String foodId;
    private String rate;
    private String comment;

    public Rating(){

    }

    public Rating(String id, String foodId, String rate, String comment) {
        this.id = id;
        this.foodId = foodId;
        this.rate = rate;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
