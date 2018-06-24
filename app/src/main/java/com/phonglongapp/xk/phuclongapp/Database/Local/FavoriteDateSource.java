package com.phonglongapp.xk.phuclongapp.Database.Local;

import com.phonglongapp.xk.phuclongapp.Database.DataSource.IFavoriteDataSource;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteDateSource implements IFavoriteDataSource {
    private FavoriteDAO favoriteDAO;
    private static FavoriteDateSource instance;

    public FavoriteDateSource(FavoriteDAO favoriteDAO){
        this.favoriteDAO = favoriteDAO;
    }

    public static FavoriteDateSource getInstance(FavoriteDAO favoriteDAO) {
        if(instance == null){
            instance = new FavoriteDateSource(favoriteDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return favoriteDAO.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDAO.isFavorite(itemId);
    }

    @Override
    public void insertCart(Favorite... favorites) {
        favoriteDAO.insertCart(favorites);
    }

    @Override
    public void deleteFavItem(Favorite favorite) {
        favoriteDAO.deleteFavItem(favorite);
    }

    @Override
    public int countFavItem() {
        return favoriteDAO.countFavItem();
    }
}
