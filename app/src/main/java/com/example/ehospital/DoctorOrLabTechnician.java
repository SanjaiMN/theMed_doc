package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorOrLabTechnician extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
    String uid="",uidcheck="";
    CardView doctorlogin,lablogin,pharmacylogin;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_or_lab_technician);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences1 = getSharedPreferences("labordoc",MODE_PRIVATE);
        String checker = sharedPreferences1.getString("prefs","");
        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String checkeruid = sharedPreferences.getString("uid","");
        if(uid.equals(checkeruid))
        {
            if(checker.equals("lab")){
                  startActivity(new Intent(DoctorOrLabTechnician.this,LabTestInfo.class));
            }
            else if(checker.equals("doctor"))
            {
                startActivity(new Intent(DoctorOrLabTechnician.this,profile.class));
            }
            else if(checker.equals("pharmacy"))
                startActivity(new Intent(DoctorOrLabTechnician.this,PharmacyInside.class));
        }
        else
        {
            sharedPreferences=getSharedPreferences("labordoc", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1=sharedPreferences.edit();
            editor1.putString("prefs","");
            editor1.commit();
            sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2=sharedPreferences.edit();
            editor2.putString("prefs",uid);
            editor2.commit();
        }
        doctorlogin=findViewById(R.id.doctorlogin);
        lablogin=findViewById(R.id.lablogin);
        pharmacylogin=findViewById(R.id.pharmacylogin);
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
}