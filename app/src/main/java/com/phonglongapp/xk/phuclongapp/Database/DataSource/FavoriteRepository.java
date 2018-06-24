package com.phonglongapp.xk.phuclongapp.Database.DataSource;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepository implements IFavoriteDataSource{

    private IFavoriteDataSource favoriteDataSource;
    public FavoriteRepository(IFavoriteDataSource favoriteDataSource){
        this.favoriteDataSource = favoriteDataSource;
    }

    private static FavoriteRepository instance;
    public static FavoriteRepository getInstance(IFavoriteDataSource favoriteDataSource){
        if(instance == null){
            instance = new FavoriteRepository(favoriteDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return favoriteDataSource.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void insertCart(Favorite... favorites) {
        favoriteDataSource.insertCart(favorites);
    }

    @Override
    public void deleteFavItem(Favorite favorite) {
        favoriteDataSource.deleteFavItem(favorite);
    }

    @Override
    public int countFavItem() {
        return favoriteDataSource.countFavItem();
    }
}
