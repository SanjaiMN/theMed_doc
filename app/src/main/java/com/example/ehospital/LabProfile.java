package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LabProfile extends AppCompatActivity
{
    TextView labname,location,propertiername,isonumber,address,phone,workinghours;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_profile);
        labname=findViewById(R.id.namelab);
        progressDialog=new ProgressDialog(LabProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        location=findViewById(R.id.Locationlab);
        propertiername=findViewById(R.id.proprietornamelab);
        isonumber=findViewById(R.id.isolab);
        address=findViewById(R.id.addresslab);
        phone=findViewById(R.id.phonelab);
        profilepic=findViewById(R.id.profilepiclab);
        workinghours=findViewById(R.id.workinghourslab);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("LaboratoryRegistrations").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                LaboratoryRegistrationDetails laboratoryRegistrationDetails=dataSnapshot.getValue(LaboratoryRegistrationDetails.class);
                labname.append(laboratoryRegistrationDetails.Laboratoryname);
                location.append(laboratoryRegistrationDetails.location);
                propertiername.append(laboratoryRegistrationDetails.proprietorname);
                isonumber.append(laboratoryRegistrationDetails.isonumber);
                address.append(laboratoryRegistrationDetails.address);
                phone.append(laboratoryRegistrationDetails.phonenumber);
                workinghours.append(laboratoryRegistrationDetails.workinghours);
                Glide.with(LabProfile.this)
                        .load(""+laboratoryRegistrationDetails.profile_pic)
                        .into(profilepic);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}