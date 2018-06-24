package com.phonglongapp.xk.phuclongapp.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FavoriteDAO {
    @Query("SELECT * FROM Favorite")
    Flowable<List<Favorite>> getFavItems();

    @Query("SELECT EXISTS(SELECT 1 FROM Favorite WHERE id=:itemId)")
    int isFavorite(int itemId);

    @Insert
    void insertCart(Favorite... favorites);

    @Delete
    void deleteFavItem(Favorite favorite);

    @Query("SELECT COUNT(*) FROM Favorite")
    int countFavItem();
}
