package com.phonglongapp.xk.phuclongapp.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Favorite")
public class Favorite {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int fId;
    @ColumnInfo(name = "idUser")
    public String uId;
    @ColumnInfo(name = "name")
    public String fName;
    @ColumnInfo(name = "imagecold")
    public String fImageCold;
    @ColumnInfo(name = "imagehot")
    public String fImageHot;
    @ColumnInfo(name = "price")
    public int fPrice;
    @ColumnInfo(name = "menuid")
    public String fMenu;

}