package com.i18nsolutions.themeddoc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class profile extends AppCompatActivity {
    FirebaseAuth fbAuth;
    CardView slots,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fbAuth = FirebaseAuth.getInstance();
        slots=findViewById(R.id.slotcard);
        profile=findViewById(R.id.profilecarddoctor);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(profile.this, "profile");
        sequence.setConfig(config);
        sequence.addSequenceItem(slots,"Check your Patient bookings here", "GOT IT");
        sequence.addSequenceItem(profile, "See your professional profile ", "GOT IT");
        sequence.start();
            slots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),Slot_Details.class));
                }
            });
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    startActivity(new Intent(profile.this,DoctorProfile.class));
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
