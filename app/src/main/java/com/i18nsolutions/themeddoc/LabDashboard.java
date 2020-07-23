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

public class LabDashboard extends AppCompatActivity
{
    CardView yourtests,profile,entermanually,uploadascsvlab,yourbookings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_dashboard);
        yourtests=findViewById(R.id.yourtestlab);
        profile=findViewById(R.id.profilecardlab);
        entermanually=findViewById(R.id.manuallab);
        uploadascsvlab=findViewById(R.id.uploadascsvlab);
        yourbookings=findViewById(R.id.yourbookingslab);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(LabDashboard.this, "LabDashboard");
        sequence.setConfig(config);
        sequence.addSequenceItem(yourtests,"Check which tests are in your concern", "GOT IT");
        sequence.addSequenceItem(profile, "Want to see your profile hit here", "GOT IT");
        sequence.addSequenceItem(entermanually,"To enter the lab tests details manually for minor corrections", "GOT IT");
        sequence.addSequenceItem(uploadascsvlab,"To upload the lab test details in csv format", "GOT IT");
        sequence.addSequenceItem(yourbookings,"Check who booked your lab for taking tests", "GOT IT");
        sequence.start();
        yourbookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LabBookings.class));
            }
        });
        uploadascsvlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LabUploadCsv.class));
            }
        });
        entermanually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LabTestInfo.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),LabProfile.class));
            }
        });
        yourtests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LabTests.class));
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