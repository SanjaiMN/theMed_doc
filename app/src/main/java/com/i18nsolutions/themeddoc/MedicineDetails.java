package com.i18nsolutions.themeddoc;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MedicineDetails implements Parcelable
{
    public String medicinename,category,medicinecategory,pharmacyname,location,money,uidmed;
    public int serialno;
    public boolean outofstock;

    public MedicineDetails(int serialno,String medicinename, String category, String medicinecategory, String pharmacyname, String location, String money,String uidmed,boolean outofstock) {
        this.serialno=serialno;
        this.medicinename = medicinename;
        this.category = category;
        this.medicinecategory = medicinecategory;
        this.pharmacyname = pharmacyname;
        this.location = location;
        this.money = money;
        this.uidmed=uidmed;
        this.outofstock=outofstock;
    }
    public MedicineDetails()
    {
    }

    protected MedicineDetails(Parcel in) {
        medicinename = in.readString();
        category = in.readString();
        medicinecategory = in.readString();
        pharmacyname = in.readString();
        location = in.readString();
        money = in.readString();
        uidmed = in.readString();
        serialno = in.readInt();
        outofstock = in.readByte() != 0;
    }

    public static final Creator<MedicineDetails> CREATOR = new Creator<MedicineDetails>() {
        @Override
        public MedicineDetails createFromParcel(Parcel in) {
            return new MedicineDetails(in);
        }

        @Override
        public MedicineDetails[] newArray(int size) {
            return new MedicineDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(medicinename);
        parcel.writeString(category);
        parcel.writeString(medicinecategory);
        parcel.writeString(pharmacyname);
        parcel.writeString(location);
        parcel.writeString(money);
        parcel.writeString(uidmed);
        parcel.writeInt(serialno);
        parcel.writeByte((byte) (outofstock ? 1 : 0));
    }
}
