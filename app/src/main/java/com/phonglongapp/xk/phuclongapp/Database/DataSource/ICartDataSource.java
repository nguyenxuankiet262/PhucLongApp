package com.phonglongapp.xk.phuclongapp.Database.DataSource;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    int isCart(int itemId, String userId);
    List<Cart> getCartItems();
    List<Cart> getCartById(int cartItemID);
    List<Cart> getCartByUserId(String userID);
    int countCartItem(String userID);
    void insertCart(Cart... carts);
    void emptyCart();
    void updateCart(Cart... carts);
    void deleteCartItem(Cart cart);
}
