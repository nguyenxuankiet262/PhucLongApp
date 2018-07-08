package com.phonglongapp.xk.phuclongapp.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.phonglongapp.xk.phuclongapp.MainActivity;
import com.phonglongapp.xk.phuclongapp.Model.Token;
import com.phonglongapp.xk.phuclongapp.R;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Utils.NotificationHelper;
import com.phonglongapp.xk.phuclongapp.ViewDrinkFragment;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService  {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference token = db.getReference("Token");
        Token temp = new Token(s,false);
        token.child(Common.CurrentUser.getId()).setValue(temp);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendNoti(remoteMessage);
            }
            else{
                sendNotiAPI26(remoteMessage);
            }
        }
    }

    private void sendNotiAPI26(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        NotificationHelper helper;
        Notification.Builder builder;
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        helper = new NotificationHelper(this);
        builder = helper.getAppNoti(notification.getTitle(),notification.getBody(),uri);

        helper.getNotificationManager().notify(new Random().nextInt(),builder.build());

    }

    private void sendNoti(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Phúc Long Coffee & Tea Exprees")
                .setSmallIcon(R.drawable.ic_local_drink_black_22dp)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setSound(uri);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(),builder.build());
    }
}
