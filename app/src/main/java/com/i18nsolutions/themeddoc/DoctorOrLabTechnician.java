package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class DoctorOrLabTechnician extends AppCompatActivity
{
    String uid="",uidcheck="";
    CardView doctorlogin,lablogin,pharmacylogin;
    SharedPreferences sharedPreferences;
    FirebaseDatabase firebaseDatabase;
    LottieAnimationView doctor,lab,pharmacy;
    DatabaseReference databaseReference,refdoc,reflab,refpharm;
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_doctor_or_lab_technician);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctor=findViewById(R.id.animationView);
        lab=findViewById(R.id.animationView1);
        pharmacy=findViewById(R.id.animationView3);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        if(Build.MANUFACTURER.toLowerCase().equals("xiaomi"))
        {
            doctor.loop(false);
            lab.loop(false);
            pharmacy.loop(false);
        }
        if(Build.VERSION.SDK_INT<26)
        {
            doctor.loop(false);
            lab.loop(false);
            pharmacy.loop(false);
        }
        SharedPreferences sharedPreferences1 = getSharedPreferences("labordoc",MODE_PRIVATE);
        String checker = sharedPreferences1.getString("prefs","");
        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String checkeruid = sharedPreferences.getString("uid","");
        if(uid.equals(checkeruid))
        {
            if(checker.equals("lab"))
            {
                startActivity(new Intent(DoctorOrLabTechnician.this,LabDashboard.class));
            }
            else if(checker.equals("doctor"))
            {
                startActivity(new Intent(DoctorOrLabTechnician.this,profile.class));
            }
            else if(checker.equals("pharmacy"))
                startActivity(new Intent(DoctorOrLabTechnician.this,PharmacyDashboard.class));
            else {
                checkdatabase();
            }
        }
        else
            {
            sharedPreferences = getSharedPreferences("labordoc", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("prefs", "");
            editor1.apply();
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences.edit();
            editor2.putString("uid", uid);
            editor2.apply();
            checkdatabase();
        }
        doctorlogin=findViewById(R.id.doctorlogin);
        lablogin=findViewById(R.id.lablogin);
        pharmacylogin=findViewById(R.id.pharmacylogin);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(DoctorOrLabTechnician.this, "DoctorOrLabTechnician");
        sequence.setConfig(config);
        sequence.addSequenceItem(doctorlogin,"This is for doctors to register for the first time...", "GOT IT");
        sequence.addSequenceItem(lablogin, "This is for laboratory login...", "GOT IT");
        sequence.addSequenceItem(pharmacylogin,"Don't worry Pharmacy is also here...", "GOT IT");
        sequence.start();
        doctorlogin.setOnClickListener(view -> startActivity(new Intent(DoctorOrLabTechnician.this,GridViewSelection.class)));
        lablogin.setOnClickListener(view -> startActivity(new Intent(DoctorOrLabTechnician.this,LabRegistration.class)));
        pharmacylogin.setOnClickListener(view -> startActivity(new Intent(DoctorOrLabTechnician.this,PharmacyRegistration.class)));
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
    void checkdatabase()
    {
        refdoc=databaseReference.child("Doctor database").child(uid);
        refdoc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    SharedPreferences sharedPreferences10 = getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences10.edit();
                    //editor1.putString("prefs", "");
                    editor1.putString("prefs", "doctor");
                    editor1.apply();
                    startActivity(new Intent(DoctorOrLabTechnician.this, profile.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reflab=databaseReference.child("LaboratoryRegistrations").child(uid);
        reflab.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    SharedPreferences sharedPreferences10 = getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences10.edit();
                    //editor1.putString("prefs", "");
                    editor1.putString("prefs", "lab");
                    editor1.apply();
                    startActivity(new Intent(DoctorOrLabTechnician.this, LabDashboard.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        refpharm=databaseReference.child("PharmacyRegistrations").child(uid);
        refpharm.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    SharedPreferences sharedPreferences10=getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences10.edit();
                    //editor.putString("prefs","");
                    editor.putString("prefs","pharmacy");
                    editor.apply();
                    startActivity(new Intent(DoctorOrLabTechnician.this, PharmacyDashboard.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}