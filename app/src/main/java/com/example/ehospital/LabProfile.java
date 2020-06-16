package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LabProfile extends AppCompatActivity
{
    TextView labname,location,propertiername,isonumber,address,phone,workinghours,ratings;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;
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
        ratings=findViewById(R.id.ratingslab);
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
                ratings.append(laboratoryRegistrationDetails.ratings+"");
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout:
            {
                new AlertDialog.Builder(this)
                        .setTitle("Really logout?")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                sharedPreferences=getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1=sharedPreferences.edit();
                                editor1.putString("prefs","");
                                firebaseAuth= FirebaseAuth.getInstance();
                                firebaseAuth.signOut();
                                finish();
                                Toast.makeText(LabProfile.this,"Logout successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LabProfile.this,MainActivity.class));
                            }
                        }).create().show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}