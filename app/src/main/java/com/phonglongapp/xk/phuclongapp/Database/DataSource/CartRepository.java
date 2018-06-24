package com.phonglongapp.xk.phuclongapp.Database.DataSource;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource){
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource){
        if(instance == null){
            instance = new CartRepository(iCartDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return iCartDataSource.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartById(int cartItemID) {
        return iCartDataSource.getCartById(cartItemID);
    }

    @Override
    public int isCart(int itemId) {
        return iCartDataSource.isCart(itemId);
    }

    @Override
    public int countCartItem() {
        return iCartDataSource.countCartItem();
    }

    @Override
    public void insertCart(Cart... carts) {
        iCartDataSource.insertCart(carts);
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public void updateCart(Cart... carts) {
        iCartDataSource.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);
    }
}
