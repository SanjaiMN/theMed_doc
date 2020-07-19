package com.i18nsolutions.themeddoc;

public class doctor_details
{
    public String name;
    public String mail;
    public String mobile;
    public String gender;
    public String specalization;
    public String working_in;
    public String age;
    public String profile_pic;
    public String uid;
    public boolean isverified;
    public String doctororlab;
    public String slottime;
    public float ratings;
    public double lats,longs;
    public int count;
    public String amount;
    public doctor_details(String name,String mail,String mobile, String gender, String specalization, String working_in, String age, String profile_pic,String uid,boolean isverified,String doctororlab,String slottime,float ratings,double lats,double longs,int count,String amount) {
        this.name = name;
        this.mail=mail;
        this.mobile=mobile;
        this.gender = gender;
        this.specalization = specalization;
        this.age = age;
        this.working_in = working_in;
        this.profile_pic=profile_pic;
        this.uid=uid;
        this.isverified=isverified;
        this.doctororlab=doctororlab;
        this.slottime=slottime;
        this.ratings=ratings;
        this.lats=lats;
        this.longs=longs;
        this.count=count;
        this.amount=amount;
    }
    public doctor_details() {

    }
}
