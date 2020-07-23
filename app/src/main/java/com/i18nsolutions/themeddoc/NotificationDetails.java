package com.i18nsolutions.themeddoc;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationDetails implements Parcelable
{
    public int taskid;
    public String name,time,date,mode;

    public NotificationDetails(int taskid, String name, String time, String date, String mode) {
        this.taskid = taskid;
        this.name = name;
        this.time = time;
        this.date = date;
        this.mode = mode;
    }

    public NotificationDetails() {
    }

    protected NotificationDetails(Parcel in) {
        taskid = in.readInt();
        name = in.readString();
        time = in.readString();
        date = in.readString();
        mode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskid);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(mode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationDetails> CREATOR = new Creator<NotificationDetails>() {
        @Override
        public NotificationDetails createFromParcel(Parcel in) {
            return new NotificationDetails(in);
        }

        @Override
        public NotificationDetails[] newArray(int size) {
            return new NotificationDetails[size];
        }
    };
}
