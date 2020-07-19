package com.i18nsolutions.themeddoc;

public class LabDetails {
    public int serialno;
    public String testname, includestestname,labname;
    public String money,nooftest,city,uidtest;
    public LabDetails(int serialno,String testname,String labname,String nooftest, String includestestname, String money,String city,String uidtest)
    {
        this.serialno=serialno;
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