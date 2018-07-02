package com.phonglongapp.xk.phuclongapp.Model;

public class Rating {
    private String id;
    private String drinkId;
    private String rate;
    private String comment;

    public Rating(){

    }

    public Rating(String id, String drinkId, String rate, String comment) {
        this.id = id;
        this.drinkId = drinkId;
        this.rate = rate;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(String drinkId) {
        this.drinkId = drinkId;
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