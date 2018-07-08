package com.phonglongapp.xk.phuclongapp.Utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.phonglongapp.xk.phuclongapp.R;

public class NotificationHelper extends ContextWrapper{
    private static final String CHANNEL_ID = "com.phonglongapp.xk.phuclongapp.XKDev";
    private static final String CHANNGEL_NAME = "Phuc Long";

    private NotificationManager notificationManager;

    public NotificationHelper (Context context){
        super(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNGEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getNotificationManager().createNotificationChannel(channel);

    }

    public NotificationManager getNotificationManager() {
        if(notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getAppNoti(String titile, String message, Uri uriSound){
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentText(message)
                .setContentTitle(titile)
                .setSmallIcon(R.drawable.ic_local_drink_black_22dp)
                .setSound(uriSound)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Phúc Long Coffee & Tea Exprees");
    }
}
