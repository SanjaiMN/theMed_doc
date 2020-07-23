package com.i18nsolutions.themeddoc;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class PharmacyProfile extends AppCompatActivity
{
    TextView pharmacyname,location,propertiername,licnumber,address,phone,mail,count,textView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    ProgressDialog progressDialog;
    String profileurl;
    ProgressBar progressBar;
    RatingBar ratings;
    SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;
    ReviewAdapter reviewAdapter;
    RecyclerView recyclerView;
    List<ReviewDetails> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Pharmacy Profile");
        setContentView(R.layout.activity_pharmacy_profile);
        pharmacyname=findViewById(R.id.namepharm);
        recyclerView=findViewById(R.id.reviewpharmacy);
        count=findViewById(R.id.noofreviewspharm);
        textView=findViewById(R.id.noreviewspp);
        progressDialog=new ProgressDialog(PharmacyProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        location=findViewById(R.id.Locationpharm);
        propertiername=findViewById(R.id.proprietornamepharm);
        licnumber=findViewById(R.id.licpharm);
        address=findViewById(R.id.addresspharm);
        phone=findViewById(R.id.phonepharm);
        profilepic=findViewById(R.id.profilepicpharm);
        mail=findViewById(R.id.mailpharm);
        ratings=findViewById(R.id.ratingBarPharm);
        progressBar=findViewById(R.id.progressBarpharmprofile);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("PharmacyRegistrations").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                progressBar.setVisibility(View.VISIBLE);
                PharmacyDetails pharmacyDetails=dataSnapshot.getValue(PharmacyDetails.class);
                pharmacyname.setText(pharmacyDetails.pharmname);
                location.setText("Location :"+pharmacyDetails.location);
                propertiername.setText("Property owned by "+pharmacyDetails.proprietorname);
                licnumber.setText("License no."+pharmacyDetails.licnumber);
                address.setText("Address: "+pharmacyDetails.address);
                phone.setText("Contact: "+pharmacyDetails.phonenumber);
                mail.setText("Mail: "+pharmacyDetails.mail);
                ratings.setRating(pharmacyDetails.ratings);
                count.setText("("+pharmacyDetails.count+" reviews)");
                profileurl=pharmacyDetails.profile_pic;
                try {
                    Glide.with(PharmacyProfile.this)
                            .load("" + pharmacyDetails.profile_pic)
                            .into(profilepic);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                catch (Exception e)
                {}
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
        reviewAdapter = new ReviewAdapter(PharmacyProfile.this,list);
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
            public void onClick(View v)
            {
                UIutils uIutils = new UIutils(PharmacyProfile.this);
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
                                firebaseAuth= FirebaseAuth.getInstance();
                                firebaseAuth.signOut();
                                finish();
                                Toast.makeText(PharmacyProfile.this,"Logout successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PharmacyProfile.this,MainActivity.class));
                            }
                        }).create().show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}