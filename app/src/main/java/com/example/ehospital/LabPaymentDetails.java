package com.example.ehospital;

import android.os.Parcel;
import android.os.Parcelable;

public class LabPaymentDetails implements Parcelable {
    public String payid;
    public String duid;
    public String puid;
    public String amount;
    public String date;
    public int serialno;
    public String walkinorhome;

    public  LabPaymentDetails(String payid, String duid, String puid, String amount, String date,int serialno,String walkinorhome) {
        this.payid = payid;
        this.duid = duid;
        this.puid = puid;
        this.amount = amount;
        this.date = date;
        this.serialno = serialno;
        this.walkinorhome=walkinorhome;
    }

    public  LabPaymentDetails() {
    }

    protected LabPaymentDetails(Parcel in) {
        payid = in.readString();
        duid = in.readString();
        puid = in.readString();
        amount = in.readString();
        date = in.readString();
        serialno = in.readInt();
        walkinorhome=in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payid);
        dest.writeString(duid);
        dest.writeString(puid);
        dest.writeString(amount);
        dest.writeString(date);
        dest.writeInt(serialno);
        dest.writeString(walkinorhome);
    }
}
