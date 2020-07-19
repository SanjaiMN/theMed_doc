package com.i18nsolutions.themeddoc;

public class PharmacyDetails
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
}