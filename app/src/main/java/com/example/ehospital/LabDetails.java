package com.example.ehospital;

public class LabDetails {
    public String testname, includestestname,labname;
    public String money,nooftest,city;

    public LabDetails(String testname,String labname,String nooftest, String includestestname, String money,String city)
    {
        this.testname = testname;
        this.labname=labname;
        this.nooftest = nooftest;
        this.includestestname = includestestname;
        this.money = money;
        this.city = city;
    }
    public LabDetails()
    {

    }
}