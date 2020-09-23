package com.example.demoforegroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MyService extends Service {
    private static final String CHANNEL_ID = "12345";
    NotificationManager manager;
    PendingIntent pendingIntent;
    CountDownTimer countDownTimer;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("0000")) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            createNotyficationChannel(manager);
            Notification notification = new NotificationCompat

                    .Builder(this, CHANNEL_ID)

                    .setContentIntent(pendingIntent)

                    .setSmallIcon(R.mipmap.ic_launcher)

                    .setContentTitle("Foreground Service")

                    .setContentText("message")

                    .setColor(Color.RED)

                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
            startForeground(123, notification);
            manager.notify(123, notification);
            startCountDown();
        }
        if (intent.getAction().equals("0000Stop")) {
            Log.e("TAG", "onStartCommand: stop");
            stopForeground(true);
            stopSelf();
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
        }
        return START_NOT_STICKY;
    }

    private void startCountDown() {

        countDownTimer = new CountDownTimer(86400000, 1000) {
            @Override
            public void onTick(long l) {
                Intent intent = new Intent();
                intent.setAction("1234");
                intent.putExtra("long", l);
                LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(intent);
                Notification notification = new NotificationCompat

                        .Builder(MyService.this, CHANNEL_ID)

                        .setContentIntent(pendingIntent)

                        .setSmallIcon(R.mipmap.ic_launcher)

                        .setContentTitle("Foreground Service")

                        .setContentText("message " + l)

                        .setColor(Color.RED)

                        .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
                manager.notify(123, notification);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void createNotyficationChannel(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel Name";
            String description = "getString(R.string.channel_description)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
