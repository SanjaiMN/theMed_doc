package com.i18nsolutions.themeddoc;

import android.os.Parcel;
import android.os.Parcelable;

public class SlotDetails implements Parcelable
{
    public String date;
    public String time;
    public String name;
    public String duid;
    public String puid;
    public String dname;
    public String specalization;
    public String shortdescription;
    public boolean cancelled;
    public boolean confirmation;
    public boolean payment;
    public String mode;
    public String amount;
    public SlotDetails(String date, String time, String name, String duid,String puid,String dname,String specalization,String shortdescription,boolean cancelled,boolean confirmation,boolean payment,String mode,String amount) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.duid = duid;
        this.puid=puid;
        this.dname=dname;
        this.specalization=specalization;
        this.shortdescription=shortdescription;
        this.cancelled=cancelled;
        this.confirmation=confirmation;
        this.payment=payment;
        this.mode=mode;
        this.amount=amount;
    }
    public SlotDetails() {
    }

    protected SlotDetails(Parcel in) {
        date = in.readString();
        time = in.readString();
        name = in.readString();
        duid = in.readString();
        puid = in.readString();
        dname = in.readString();
        specalization = in.readString();
        shortdescription = in.readString();
        cancelled = in.readByte() != 0;
        confirmation = in.readByte() != 0;
        payment = in.readByte() != 0;
        mode = in.readString();
        amount = in.readString();
    }

    public static final Creator<SlotDetails> CREATOR = new Creator<SlotDetails>() {
        @Override
        public SlotDetails createFromParcel(Parcel in) {
            return new SlotDetails(in);
        }

        @Override
        public SlotDetails[] newArray(int size) {
            return new SlotDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(name);
        dest.writeString(duid);
        dest.writeString(puid);
        dest.writeString(dname);
        dest.writeString(specalization);
        dest.writeString(shortdescription);
        dest.writeByte((byte) (cancelled ? 1 : 0));
        dest.writeByte((byte) (confirmation ? 1 : 0));
        dest.writeByte((byte) (payment ? 1 : 0));
        dest.writeString(mode);
        dest.writeString(amount);
    }
}
