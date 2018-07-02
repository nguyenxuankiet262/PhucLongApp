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
    public static boolean checkDrinkFragmentOpen;
    public static String idDrink;

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

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }
}
