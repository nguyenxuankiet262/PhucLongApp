package com.phonglongapp.xk.phuclongapp.Model;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;

import java.util.List;

public class Order {
    private String name;
    private String address;
    private String phone;
    private String price;
    private String status;
    private String note;
    private String payment;
    List<Cart> cartList;

    public Order(){

    }

    public Order(String name, String address, String phone, String price, String status, String note, String payment, List<Cart> cartList) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.price = price;
        this.status = status;
        this.note = note;
        this.payment = payment;
        this.cartList = cartList;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
}