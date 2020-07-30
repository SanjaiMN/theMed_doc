package com.i18nsolutions.themeddoc;

public class PatientDetails
{
        public String name;
        public String uid;
        public String email;
        public String gender;
        public String profilepic;
        public String age;
        public String phone;

        public PatientDetails(String name, String uid, String email, String gender, String profilepic, String age,String phone)
        {
            this.name = name;
            this.uid = uid;
            this.email = email;
            this.gender = gender;
            this.profilepic = profilepic;
            this.age = age;
            this.phone=phone;
        }
        public PatientDetails()
        {
        }
}

