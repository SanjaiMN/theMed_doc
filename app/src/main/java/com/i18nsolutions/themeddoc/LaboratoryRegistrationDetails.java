package com.i18nsolutions.themeddoc;

import android.os.Parcel;
import android.os.Parcelable;

public class LaboratoryRegistrationDetails implements Parcelable {
    public String Laboratoryname,mail,location,proprietorname,isonumber,doctororlab,profile_pic,address,phonenumber,workinghours,uidlabreg;
    public double lats,longs;
    public float ratings;
    public int count;
    public boolean isverified;
    public LaboratoryRegistrationDetails(String laboratoryname,String mail, String location, String proprietorname, String isonumber,String doctororlab,String profile_pic,String address,String phonenumber,String workinghours,String uidlabreg,float ratings,double lats,double longs,int count,boolean isverified)
    {
        Laboratoryname = laboratoryname;
        this.mail=mail;
        this.location = location;
        this.proprietorname = proprietorname;
        this.isonumber = isonumber;
        this.doctororlab=doctororlab;
        this.profile_pic=profile_pic;
        this.address=address;
        this.phonenumber=phonenumber;
        this.workinghours=workinghours;
        this.uidlabreg=uidlabreg;
        this.ratings=ratings;
        this.lats=lats;
        this.longs=longs;
        this.count=count;
        this.isverified=isverified;
    }
    public LaboratoryRegistrationDetails()
    {
    }

    protected LaboratoryRegistrationDetails(Parcel in) {
        Laboratoryname = in.readString();
        mail = in.readString();
        location = in.readString();
        proprietorname = in.readString();
        isonumber = in.readString();
        doctororlab = in.readString();
        profile_pic = in.readString();
        address = in.readString();
        phonenumber = in.readString();
        workinghours = in.readString();
        uidlabreg = in.readString();
        lats = in.readDouble();
        longs = in.readDouble();
        ratings = in.readFloat();
        count = in.readInt();
        isverified = in.readByte() != 0;
    }

    public static final Creator<LaboratoryRegistrationDetails> CREATOR = new Creator<LaboratoryRegistrationDetails>() {
        @Override
        public LaboratoryRegistrationDetails createFromParcel(Parcel in) {
            return new LaboratoryRegistrationDetails(in);
        }

        @Override
        public LaboratoryRegistrationDetails[] newArray(int size) {
            return new LaboratoryRegistrationDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Laboratoryname);
        parcel.writeString(mail);
        parcel.writeString(location);
        parcel.writeString(proprietorname);
        parcel.writeString(isonumber);
        parcel.writeString(doctororlab);
        parcel.writeString(profile_pic);
        parcel.writeString(address);
        parcel.writeString(phonenumber);
        parcel.writeString(workinghours);
        parcel.writeString(uidlabreg);
        parcel.writeDouble(lats);
        parcel.writeDouble(longs);
        parcel.writeFloat(ratings);
        parcel.writeInt(count);
        parcel.writeByte((byte) (isverified ? 1 : 0));
    }
}
