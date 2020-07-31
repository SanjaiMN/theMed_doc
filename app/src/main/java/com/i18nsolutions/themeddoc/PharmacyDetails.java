package com.i18nsolutions.themeddoc;

import android.os.Parcel;
import android.os.Parcelable;

public class PharmacyDetails implements Parcelable
{
    public String pharmname,mail,location,proprietorname,licnumber,doctororlaborpharm,profile_pic,address,phonenumber,uidpharmreg;
    public double lats,longs;
    public float ratings;
    public int count;
    public  boolean isverified;
    public PharmacyDetails(String pharmname,String mail, String location, String proprietorname, String licnumber,String doctororlab,String profile_pic,String address,String phonenumber,String uidpharmreg,float ratings,double lats,double longs,int count,boolean isverified) {
        this.pharmname = pharmname;
        this.mail=mail;
        this.location = location;
        this.proprietorname = proprietorname;
        this.licnumber = licnumber;
        this.doctororlaborpharm=doctororlab;
        this.profile_pic=profile_pic;
        this.address=address;
        this.phonenumber=phonenumber;
        this.uidpharmreg=uidpharmreg;
        this.ratings=ratings;
        this.lats=lats;
        this.longs=longs;
        this.count=count;
        this.isverified=isverified;
    }
    public PharmacyDetails() {
    }

    protected PharmacyDetails(Parcel in) {
        pharmname = in.readString();
        mail = in.readString();
        location = in.readString();
        proprietorname = in.readString();
        licnumber = in.readString();
        doctororlaborpharm = in.readString();
        profile_pic = in.readString();
        address = in.readString();
        phonenumber = in.readString();
        uidpharmreg = in.readString();
        lats = in.readDouble();
        longs = in.readDouble();
        ratings = in.readFloat();
        count = in.readInt();
        isverified = in.readByte() != 0;
    }

    public static final Creator<PharmacyDetails> CREATOR = new Creator<PharmacyDetails>() {
        @Override
        public PharmacyDetails createFromParcel(Parcel in) {
            return new PharmacyDetails(in);
        }

        @Override
        public PharmacyDetails[] newArray(int size) {
            return new PharmacyDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pharmname);
        parcel.writeString(mail);
        parcel.writeString(location);
        parcel.writeString(proprietorname);
        parcel.writeString(licnumber);
        parcel.writeString(doctororlaborpharm);
        parcel.writeString(profile_pic);
        parcel.writeString(address);
        parcel.writeString(phonenumber);
        parcel.writeString(uidpharmreg);
        parcel.writeDouble(lats);
        parcel.writeDouble(longs);
        parcel.writeFloat(ratings);
        parcel.writeInt(count);
        parcel.writeByte((byte) (isverified ? 1 : 0));
    }
}