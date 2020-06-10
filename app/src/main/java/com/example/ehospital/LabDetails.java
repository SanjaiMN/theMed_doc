package com.example.ehospital;

public class LabDetails {
    public String testname, includestestname,labname;
    public String money,nooftest,city,uidtest;
    public LabDetails(String testname,String labname,String nooftest, String includestestname, String money,String city,String uidtest)
    {
        this.testname = testname;
        this.labname=labname;
        this.nooftest = nooftest;
        this.includestestname = includestestname;
        this.money = money;
        this.city = city;
        this.uidtest=uidtest;
    }
    public LabDetails()
    {
    }
}