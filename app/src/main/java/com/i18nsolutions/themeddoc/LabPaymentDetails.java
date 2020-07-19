package com.i18nsolutions.themeddoc;

import android.os.Parcel;
import android.os.Parcelable;

public class LabPaymentDetails implements Parcelable {
    public String payid;
    public String duid;
    public String puid;
    public String amount;
    public String date;
    public String serialno,count;
    public String walkinorhome,lats,longs;
    public boolean paymentdone;
    public boolean isdelivered;

    public  LabPaymentDetails(String payid, String duid, String puid, String amount, String date,String lats,String longs,String serialno,String count,String walkinorhome,boolean paymentdone,boolean isdelivered) {
        this.payid = payid;
        this.duid = duid;
        this.puid = puid;
        this.amount = amount;
        this.date = date;
        this.lats=lats;
        this.longs=longs;
        this.serialno = serialno;
        this.count=count;
        this.walkinorhome=walkinorhome;
        this.paymentdone=paymentdone;
        this.isdelivered=isdelivered;
    }
    public  LabPaymentDetails() {
    }

    protected LabPaymentDetails(Parcel in) {
        payid = in.readString();
        duid = in.readString();
        puid = in.readString();
        amount = in.readString();
        date = in.readString();
        serialno = in.readString();
        count = in.readString();
        walkinorhome = in.readString();
        lats = in.readString();
        longs = in.readString();
        paymentdone = in.readByte() != 0;
        isdelivered = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payid);
        dest.writeString(duid);
        dest.writeString(puid);
        dest.writeString(amount);
        dest.writeString(date);
        dest.writeString(serialno);
        dest.writeString(count);
        dest.writeString(walkinorhome);
        dest.writeString(lats);
        dest.writeString(longs);
        dest.writeByte((byte) (paymentdone ? 1 : 0));
        dest.writeByte((byte) (isdelivered ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LabPaymentDetails> CREATOR = new Creator<LabPaymentDetails>() {
        @Override
        public LabPaymentDetails createFromParcel(Parcel in) {
            return new LabPaymentDetails(in);
        }

        @Override
        public LabPaymentDetails[] newArray(int size) {
            return new LabPaymentDetails[size];
        }
    };
}
