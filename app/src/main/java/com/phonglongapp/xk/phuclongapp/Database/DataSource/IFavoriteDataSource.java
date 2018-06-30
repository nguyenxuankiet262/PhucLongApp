package com.phonglongapp.xk.phuclongapp.Database.DataSource;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {
    List<Favorite> getFavItems();
    List<Favorite> getFavItemsByUserID(String userID);
    int isFavorite(int itemId, String userId);
    void insertCart(Favorite... favorites);
    void deleteFavItem(Favorite favorite);
    int countFavItem(String userID);
}
