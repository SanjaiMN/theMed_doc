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

public class PharmacyProfile extends AppCompatActivity
{
    TextView pharmacyname,location,propertiername,licnumber,address,phone,mail,ratings;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_profile);
        pharmacyname=findViewById(R.id.namepharm);
        progressDialog=new ProgressDialog(PharmacyProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        location=findViewById(R.id.Locationpharm);
        propertiername=findViewById(R.id.proprietornamepharm);
        licnumber=findViewById(R.id.licpharm);
        address=findViewById(R.id.addresspharm);
        phone=findViewById(R.id.phonepharm);
        profilepic=findViewById(R.id.profilepicpharm);
        mail=findViewById(R.id.mailpharm);
        ratings=findViewById(R.id.ratingspharm);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("PharmacyRegistrations").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                PharmacyDetails pharmacyDetails=dataSnapshot.getValue(PharmacyDetails.class);
                pharmacyname.append(pharmacyDetails.pharmname);
                location.append(pharmacyDetails.location);
                propertiername.append(pharmacyDetails.proprietorname);
                licnumber.append(pharmacyDetails.licnumber);
                address.append(pharmacyDetails.address);
                phone.append(pharmacyDetails.phonenumber);
                mail.append(pharmacyDetails.mail);
                ratings.append(pharmacyDetails.ratings+"");
                Glide.with(PharmacyProfile.this)
                        .load(""+pharmacyDetails.profile_pic)
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