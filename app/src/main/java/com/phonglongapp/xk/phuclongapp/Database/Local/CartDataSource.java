package com.phonglongapp.xk.phuclongapp.Database.Local;

import com.phonglongapp.xk.phuclongapp.Database.DataSource.ICartDataSource;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO){
        this.cartDAO = cartDAO;
    }
    public static CartDataSource getInstance(CartDAO cartDAO){
        if(instance == null){
            instance = new CartDataSource(cartDAO);
        }
        return instance;
    }

    @Override
    public int isCart(int itemId) {
        return cartDAO.isCart(itemId);
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartById(int cartItemID) {
        return cartDAO.getCartById(cartItemID);
    }

    @Override
    public int countCartItem() {
        return cartDAO.countCartItem();
    }

    @Override
    public void insertCart(Cart... carts) {
        cartDAO.insertCart(carts);
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart(carts);
    }


    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);
    }
}
