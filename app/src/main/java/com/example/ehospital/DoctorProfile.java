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

public class DoctorProfile extends AppCompatActivity
{
    String uid,profile_pic;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    TextView namee,agee,working_inn,specalizationn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        progressDialog=new ProgressDialog(DoctorProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        profilepic=findViewById(R.id.profilepic);
        namee=findViewById(R.id.namee);
        agee=findViewById(R.id.agee);
        working_inn=findViewById(R.id.working_inn);
        specalizationn=findViewById(R.id.specalizationn);
        profilepic=findViewById(R.id.profilepic);
        firebaseDatabase=FirebaseDatabase.getInstance();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference=firebaseDatabase.getReference().child("Doctor database").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                doctor_details doctor_details=dataSnapshot.getValue(doctor_details.class);
                namee.append(doctor_details.name);
                agee.append(doctor_details.age);
                working_inn.append(doctor_details.working_in);
                specalizationn.append(doctor_details.specalization);
                profile_pic=doctor_details.profile_pic;
                Glide.with(DoctorProfile.this)
                        .load(""+profile_pic)
                        .into(profilepic);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}