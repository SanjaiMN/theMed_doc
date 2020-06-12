package com.example.ehospital;

import java.io.Serializable;

public class MedicineDetails implements Serializable
{
    public String medicinename,category,medicinecategory,pharmacyname,location,money,uidmed;

    public MedicineDetails(String medicinename, String category, String medicinecategory, String pharmacyname, String location, String money,String uidmed) {
        this.medicinename = medicinename;
        this.category = category;
        this.medicinecategory = medicinecategory;
        this.pharmacyname = pharmacyname;
        this.location = location;
        this.money = money;
        this.uidmed=uidmed;
    }

    public MedicineDetails() {
    }
}
