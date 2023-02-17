package com.example.mspy;

import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "rubelliumm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button click ....
        Button opensettingbtn = findViewById(R.id.openNotificationSettingBtn);
        opensettingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        });
        //end button ......
        TextView tv = findViewById(R.id.textView);
        if (isNotificationAccessGranted(getApplicationContext())){
            tv.setText("has permission");
        }
        else{
            tv.setText("dont have permission");
        }

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (isNotificationAccessGranted(getApplicationContext())) {
            // Start the service
            Intent intent = new Intent(this, bgNotificationService.class);
            this.startService(intent);
        } else {
            // Prompt the user to grant permission
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            this.startActivity(intent);
        }
//        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
//        this.startActivity(intent);
//        Boolean a =  isNotificationAccessGranted(getApplicationContext());
//        Log.d(TAG, "onCreate: "+ String.valueOf(a));


    }
    public static boolean isNotificationAccessGranted(Context context) {
        String enabledListeners = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        String packageName = context.getPackageName();

        return enabledListeners != null && enabledListeners.contains(packageName);
    }

}