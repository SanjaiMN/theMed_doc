package com.example.ehospital;
public class LaboratoryRegistrationDetails
{
    public String Laboratoryname,location,proprietorname,isonumber,doctororlab,profile_pic,address,phonenumber,workinghours;
    public LaboratoryRegistrationDetails(String laboratoryname, String location, String proprietorname, String isonumber,String doctororlab,String profile_pic,String address,String phonenumber,String workinghours) {
        Laboratoryname = laboratoryname;
        this.location = location;
        this.proprietorname = proprietorname;
        this.isonumber = isonumber;
        this.doctororlab=doctororlab;
        this.profile_pic=profile_pic;
        this.address=address;
        this.phonenumber=phonenumber;
        this.workinghours=workinghours;
    }
    public LaboratoryRegistrationDetails() {
    }
}
