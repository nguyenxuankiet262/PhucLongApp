package com.phonglongapp.xk.phuclongapp.Database.DataSource;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    int isCart(int itemId);
    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartById(int cartItemID);
    int countCartItem();
    void insertCart(Cart... carts);
    void emptyCart();
    void updateCart(Cart... carts);
    void deleteCartItem(Cart cart);
}
