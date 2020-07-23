package com.i18nsolutions.themeddoc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskAlarmCreator
{
   public NotificationDetails notificationDetails;
   public Context context;

    public TaskAlarmCreator(NotificationDetails notificationDetails, Context context) {
        this.notificationDetails = notificationDetails;
        this.context = context;
    }

    public TaskAlarmCreator(Context context) {
        this.context = context;
    }

    public TaskAlarmCreator() {
    }
    void createAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra("taskid",notificationDetails.taskid);
        intent.putExtra("name",notificationDetails.name);
        intent.putExtra("date",notificationDetails.date);
        intent.putExtra("time",notificationDetails.time);
        intent.putExtra("mode",notificationDetails.mode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,notificationDetails.taskid,intent,0);
        long time=0;

        try{
            time=extractTime(notificationDetails.time);
        }
        catch (Exception e){}
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,time,pendingIntent);
    }
    long extractTime(String time) throws ParseException {
        Date dest=new SimpleDateFormat("HH:mm").parse(time);
        Calendar currentTime=Calendar.getInstance();
        Calendar tempTime=Calendar.getInstance();
        tempTime.setTime(dest);
        Calendar destTime=Calendar.getInstance();
        destTime.set(Calendar.HOUR_OF_DAY,tempTime.get(Calendar.HOUR_OF_DAY));
        destTime.set(Calendar.MINUTE,tempTime.get(Calendar.MINUTE));
        destTime.set(Calendar.SECOND,0);
        if(destTime.before(currentTime))                    //if hour is passed,say current time is 14:00 and
            destTime.add(Calendar.DATE,1);          //task is at 13:00  create alarm for nextDay
        return destTime.getTimeInMillis();
        //return time in millis as this time is used by alarm manager
    }

}
