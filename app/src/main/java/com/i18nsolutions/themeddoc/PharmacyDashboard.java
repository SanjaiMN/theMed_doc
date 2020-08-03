package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class PharmacyDashboard extends AppCompatActivity
{
    CardView yourmedicines,profile,entermanually,uploadascsv,yourbookings;
    String uid,city;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pharmacy_dashboard);
        progressDialog= new ProgressDialog(PharmacyDashboard.this);
        progressDialog.setMessage("Gathering your contents...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        yourmedicines=findViewById(R.id.yourmedicines);
        profile=findViewById(R.id.profilecardpharm);
        entermanually=findViewById(R.id.manualpharmacy);
        uploadascsv=findViewById(R.id.uploadascsv);
        yourbookings=findViewById(R.id.yourbookingspharm);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("PharmacyRegistrations").child(uid).child("location");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                city=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
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
                        Intent intent=new Intent(getApplicationContext(),MedicineLists.class);
                        try {
                            if (!city.isEmpty()){
                                intent.putExtra("cityname", city);
                                startActivity(intent);
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(PharmacyDashboard.this,"Wait for a moment",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                progressDialog.dismiss();
            }
        }, 3000);
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater layoutInflater = getMenuInflater();
        layoutInflater.inflate(R.menu.aboutmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                startActivity(new Intent(PharmacyDashboard.this,About.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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