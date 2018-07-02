package com.phonglongapp.xk.phuclongapp.Database.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Favorite;

@Database(entities = {Cart.class,Favorite.class}, version = 1)
public abstract class DrinkRoomDatabase extends RoomDatabase {
    private static DrinkRoomDatabase sCartDatabase;
    public static final String DATABASE_NAME = "Room-database";

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();

    public static DrinkRoomDatabase getInstance(Context context) {
        if (sCartDatabase == null) {
            sCartDatabase = Room.databaseBuilder(context, DrinkRoomDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sCartDatabase;
    }

}
