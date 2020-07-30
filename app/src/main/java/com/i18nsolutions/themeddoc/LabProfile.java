package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LabProfile extends AppCompatActivity
{
    TextView labname,location,propertiername,isonumber,address,phone,workinghours,count;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    SharedPreferences sharedPreferences;
    TextView textView;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String profileurl;
    RatingBar ratings;
    ReviewAdapter reviewAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<ReviewDetails> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Laboratory Profile");
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_lab_profile);
        labname=findViewById(R.id.namelab);
        progressDialog=new ProgressDialog(LabProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        textView=findViewById(R.id.noreviewslp);
        count=findViewById(R.id.noofreviewslab);
        recyclerView=findViewById(R.id.reviewslab);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        location=findViewById(R.id.Locationlab);
        propertiername=findViewById(R.id.proprietornamelab);
        isonumber=findViewById(R.id.isolab);
        address=findViewById(R.id.addresslab);
        phone=findViewById(R.id.phonelab);
        profilepic=findViewById(R.id.profilepiclab);
        workinghours=findViewById(R.id.workinghourslab);
        ratings=findViewById(R.id.ratingBarLab);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("LaboratoryRegistrations").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                LaboratoryRegistrationDetails laboratoryRegistrationDetails=dataSnapshot.getValue(LaboratoryRegistrationDetails.class);
                labname.setText("  "+laboratoryRegistrationDetails.Laboratoryname);
                location.setText("  "+laboratoryRegistrationDetails.location);
                propertiername.setText("  "+laboratoryRegistrationDetails.proprietorname);
                isonumber.setText("  "+laboratoryRegistrationDetails.isonumber);
                address.setText("  "+laboratoryRegistrationDetails.address);
                phone.setText("  "+laboratoryRegistrationDetails.phonenumber);
                workinghours.setText("  "+laboratoryRegistrationDetails.workinghours);
                ratings.setRating(laboratoryRegistrationDetails.ratings);
                count.setText("("+laboratoryRegistrationDetails.count+" reviews)");
                profileurl=laboratoryRegistrationDetails.profile_pic;
                try {
                    Glide.with(LabProfile.this)
                            .load(""+laboratoryRegistrationDetails.profile_pic)
                            .into(profilepic);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                catch (Exception e){}
                progressDialog.dismiss();
                databaseReference.child("Reviews").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1)
                    {
                        list.clear();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            ReviewDetails reviewDetails = dataSnapshot2.getValue(ReviewDetails.class);
                            if (reviewDetails.comment.length()==0)
                                reviewDetails.comment="No Comments";
                            list.add(reviewDetails);
                        }
                        reviewAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(reviewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        reviewAdapter = new ReviewAdapter(LabProfile.this,list);
        reviewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(reviewAdapter.getItemCount()==0)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.INVISIBLE);
            }
        });
        reviewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(reviewAdapter);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIutils uIutils = new UIutils(LabProfile.this);
                uIutils.showPhoto(Uri.parse(profileurl));
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
                                editor1.apply();
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