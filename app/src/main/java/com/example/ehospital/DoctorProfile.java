package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DoctorProfile extends AppCompatActivity
{
    String uid,profile_pic;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    TextView namee,agee,working_inn,specalizationn,ratings;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        progressDialog=new ProgressDialog(DoctorProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        profilepic=findViewById(R.id.profilepiclab);
        namee=findViewById(R.id.namee);
        agee=findViewById(R.id.agee);
        working_inn=findViewById(R.id.working_inn);
        specalizationn=findViewById(R.id.specalizationn);
        profilepic=findViewById(R.id.profilepiclab);
        ratings=findViewById(R.id.ratingsdoc);
        progressBar=findViewById(R.id.doctorprofilepb);
        firebaseDatabase=FirebaseDatabase.getInstance();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference=firebaseDatabase.getReference().child("Doctor database").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                progressBar.setVisibility(View.VISIBLE);
                doctor_details doctor_details=dataSnapshot.getValue(doctor_details.class);
                namee.append(doctor_details.name);
                agee.append(doctor_details.age);
                working_inn.append(doctor_details.working_in);
                specalizationn.append(doctor_details.specalization);
                ratings.append(String.valueOf(doctor_details.ratings));
                profile_pic=doctor_details.profile_pic;
                Glide.with(DoctorProfile.this)
                        .load(""+profile_pic)
                        .into(profilepic);
                progressDialog.dismiss();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
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
                                FirebaseAuth.getInstance().signOut();
                                finish();
                                Toast.makeText(DoctorProfile.this,"Logout successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DoctorProfile.this,MainActivity.class));
                            }
                        }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}