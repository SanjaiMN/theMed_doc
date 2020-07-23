package com.i18nsolutions.themeddoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper
{
    public  static final int currentdpversion=2;
    public static final String dbname="Sanjai.db";
    public static final String tablename="Notify";
    public static final String column1="patientname";
    public static final String date="Date";
    public static final String mode="Mode";
    public static final String time="Time";
    public static final String taskid="Taskid";

    public DBHelper(@Nullable Context context)
    {
        super(context, dbname, null, currentdpversion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("create table "+tablename+"("+taskid+" integer primary key,"+column1+" text,"+date+" text,"+mode+" text,"+time+" text);");
        //db.execSQL("create table "+TABLE_NAME+"("+COLUMN4+ " integer primary key,"+COLUMN1+" text,"+COLUMN2+" text,"+COLUMN3+" text,"+COLUMN5+" integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tablename);
        onCreate(sqLiteDatabase);
    }
    public boolean insert(NotificationDetails task)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(taskid,task.taskid);
        contentValues.put(column1,task.name);
        contentValues.put(date,task.date);
        contentValues.put(time,task.time);
        contentValues.put(mode,task.mode);
        long val = database.insert(tablename,null,contentValues);
        if(val!=-1)
        {
            return true;
        }
        return false;
    }
    public ArrayList<NotificationDetails> get()
    {
        ArrayList<NotificationDetails> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+tablename,null);
        while(cursor.moveToNext())
        {
            NotificationDetails notificationDetails= new NotificationDetails(Integer.parseInt(taskid),column1,time,date,mode);
            arrayList.add(notificationDetails);
        }
        return arrayList;
    }
}
