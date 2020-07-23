package com.i18nsolutions.themeddoc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class PharmacyDashboard extends AppCompatActivity
{
    CardView yourmedicines,profile,entermanually,uploadascsv,yourbookings;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_dashboard);
        yourmedicines=findViewById(R.id.yourmedicines);
        profile=findViewById(R.id.profilecardpharm);
        entermanually=findViewById(R.id.manualpharmacy);
        uploadascsv=findViewById(R.id.uploadascsv);
        yourbookings=findViewById(R.id.yourbookingspharm);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(PharmacyDashboard.this, "PharmacyDashboard");
        sequence.setConfig(config);
        sequence.addSequenceItem(yourmedicines,"Check which medicines are in your concern", "GOT IT");
        sequence.addSequenceItem(profile, "Want to see your profile hit here", "GOT IT");
        sequence.addSequenceItem(entermanually,"To enter the Medicine details manually for minor corrections", "GOT IT");
        sequence.addSequenceItem(uploadascsv,"To upload the Medicine details in csv format", "GOT IT");
        sequence.addSequenceItem(yourbookings,"Check who booked your pharmacy for medicines", "GOT IT");
        sequence.start();
        yourbookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PharmacyBookings.class));
            }
        });
        uploadascsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PharmacyInside.class));
            }
        });
        entermanually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PharamacyManual.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),PharmacyProfile.class));
            }
        });
        yourmedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MedicineLists.class));
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        moveTaskToBack(true);
                    }
                }).create().show();
    }
}