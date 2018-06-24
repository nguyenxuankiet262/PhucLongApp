package com.phonglongapp.xk.phuclongapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.phonglongapp.xk.phuclongapp.Database.DataSource.CartRepository;
import com.phonglongapp.xk.phuclongapp.Database.DataSource.FavoriteRepository;
import com.phonglongapp.xk.phuclongapp.Database.Local.DrinkRoomDatabase;
import com.phonglongapp.xk.phuclongapp.Model.Coordinates;
import com.phonglongapp.xk.phuclongapp.Model.User;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Common {
    public static DrinkRoomDatabase drinkroomDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;
    public static int BackPress;
    public static View parentFavLayout;
    public static String id_cate;

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            for(int i = 0; i<infos.length;i++) {
                if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
    public static double ConvertStringToDouble(String s){
        return Double.parseDouble(s);
    }

    public static Map<String,Coordinates> coordinatesStringMap;
    public static String ConvertIntToMoney(String money){
        return NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(money)) + " VNĐ";
    }

    public static User CurrentUser;
}
