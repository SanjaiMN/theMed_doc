package com.example.ehospital;
public class LaboratoryRegistrationDetails
{
    public String Laboratoryname,mail,location,proprietorname,isonumber,doctororlab,profile_pic,address,phonenumber,workinghours,uidlabreg;
    public double lats,longs;
    public float ratings;
    public LaboratoryRegistrationDetails(String laboratoryname,String mail, String location, String proprietorname, String isonumber,String doctororlab,String profile_pic,String address,String phonenumber,String workinghours,String uidlabreg,float ratings,double lats,double longs)
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
    }
    public LaboratoryRegistrationDetails()
    {
    }
}
