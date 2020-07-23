package com.i18nsolutions.themeddoc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    final String channelid="1";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long [] arr = new long[]{1000,1000,1000,1000,1000,1000};
        createNotificationChannel(context);
        NotificationDetails notificationDetails = buildtask(intent);
        String title = "Slot Remainder";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelid).setSmallIcon(R.drawable.logofinalfinal).setContentTitle(title).setContentText(notificationDetails.name).setSound(uri).setVibrate(arr).setSubText(notificationDetails.date+" "+notificationDetails.time+" by"+notificationDetails.mode).setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(notificationDetails.taskid,builder.build());
    }
    void createNotificationChannel(Context context)
    {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O)
        {

            CharSequence charSequence = "Slot Notification";
            String desc ="Hey Someone Booked you,Check who!!";
            int important = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelid,charSequence,important);
            notificationChannel.setDescription(desc);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private NotificationDetails buildtask(Intent intent)
    {
        String name = intent.getStringExtra("name");
        int taskid = intent.getIntExtra("taskid",0);
        String date= intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String mode = intent.getStringExtra("mode");
        return new NotificationDetails(taskid,name,time,date,mode);
    }
}
