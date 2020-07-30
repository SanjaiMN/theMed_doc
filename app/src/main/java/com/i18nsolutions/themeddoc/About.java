package com.i18nsolutions.themeddoc;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About  extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.josefin_sans_bold);
        Element versionElement = new Element();
        versionElement.setTitle("Version name:"+BuildConfig.VERSION_NAME);
        versionElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(About.this,"  This is the Latest Release  ",Toast.LENGTH_SHORT).show();
            }
        });
        Element developer = new Element();
        developer.setTitle("Developer:"+"Sanjai MN");
        Element description=new Element();
        description.setTitle("This app contains three handles namely\n" +
                "----Doctors,Laboratories and Pharmacy\n" +
                "For doctors,\n" +
                "They can communicate to patient through video call or chat based on the patient who booked the doctor. They can send them prescription after the interaction.\n" +
                "For Lab and Pharmacy\n" +
                "They can use this to see their booking details and responds to the customer by delivering the respective products.they can manage the stock information as well.\n" +
                "This app will make work easier through this.This is not for the normal users,This app should only used by the Doctors ,Laboratories and Pharmacists\n" +
                "A separate app for patients also available...");

        View aboutpage=new AboutPage(this)
                .isRTL(false)
                .enableDarkMode(false)
                .setDescription("Med Heal - For Doctors,Laboratory & Pharmacy")
                .setCustomFont(typeface)
                .addItem(description)
                .addItem(developer)
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("i18nsolution.gmail.com")
                .addWebsite("https://www.i18nsolutions.in/")
                .addFacebook("i18nsolutions")
                .addTwitter("@i18nsolutions")
                .addYoutube("UCFzaYF8EQfaUUie1fnz8ojQ")
                .addPlayStore("com.i18nsolutions.themeddoc")
                .addGitHub("SanjaiMN")
                .create();
        setContentView(aboutpage);
    }
}
