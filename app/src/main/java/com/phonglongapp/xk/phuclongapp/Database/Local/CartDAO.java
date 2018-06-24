package com.phonglongapp.xk.phuclongapp.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {
    @Query("SELECT EXISTS(SELECT 1 FROM Cart WHERE id=:itemId)")
    int isCart(int itemId);

    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart WHERE id = :cartItemID")
    Flowable<List<Cart>> getCartById(int cartItemID);

    @Query("SELECT COUNT(*) FROM Cart")
    int countCartItem();

    @Insert
    void insertCart(Cart... carts);

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Update
    void updateCart(Cart... carts);

    @Delete
    void deleteCartItem(Cart cart);
}
