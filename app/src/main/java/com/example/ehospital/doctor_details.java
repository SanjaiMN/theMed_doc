package com.example.ehospital;

import java.util.StringTokenizer;

public class doctor_details {
    public String name;
    public String gender;
    public String specalization;
    public String working_in;
    public String age;
    public String profile_pic;
    public String SessionId;
    public String TokenId;
    public String uid;
    public String Request;
    public String doctororlab;
    public doctor_details(String name, String gender, String specalization, String working_in, String age, String profile_pic, String SessionId, String TokenId,String uid,String Request,String doctororlab) {
        this.name = name;
        this.gender = gender;
        this.specalization = specalization;
        this.age = age;
        this.working_in = working_in;
        this.profile_pic=profile_pic;
        this.SessionId=SessionId;
        this.TokenId=TokenId;
        this.uid=uid;
        this.Request=Request;
        this.doctororlab=doctororlab;
    }

    public doctor_details() {

    }

}