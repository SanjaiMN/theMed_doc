package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
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

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class DoctorProfile extends AppCompatActivity
{
    String uid,profile_pic;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    TextView namee,agee,working_inn,specalizationn,reviewcount,patientscount,textView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    ImageButton editprofile;
    RatingBar ratings;
    SharedPreferences sharedPreferences;
    List<ReviewDetails> list=new ArrayList<>();
    ReviewAdapter reviewAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Doctor Profile");
        setContentView(R.layout.activity_doctor_profile);
        progressDialog=new ProgressDialog(DoctorProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        recyclerView=findViewById(R.id.reviews);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        profilepic=findViewById(R.id.profilepiclab);
        namee=findViewById(R.id.namee);
        agee=findViewById(R.id.agee);
        textView=findViewById(R.id.noreviewsdp);
        patientscount=findViewById(R.id.patientseen);
        reviewcount=findViewById(R.id.reviewcountdoctor);
        working_inn=findViewById(R.id.working_inn);
        specalizationn=findViewById(R.id.specalizationn);
        profilepic=findViewById(R.id.profilepiclab);
        ratings=findViewById(R.id.ratingBarLab);
        progressBar=findViewById(R.id.doctorprofilepb);
       // editprofile=findViewById(R.id.editprofile);
        firebaseDatabase=FirebaseDatabase.getInstance();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference1=FirebaseDatabase.getInstance().getReference().child("Payment Report").child("doctor booking").child(uid);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                patientscount.setText(dataSnapshot.getChildrenCount()+" patients seen");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                patientscount.setText("0 patients seen");
            }
        });
        databaseReference=firebaseDatabase.getReference().child("Doctor database").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                doctor_details doctor_details=dataSnapshot.getValue(doctor_details.class);
                namee.setText("Dr ."+doctor_details.name);
                agee.setText(doctor_details.age+" years old");
                working_inn.setText("Working in "+doctor_details.working_in);
                specalizationn.setText("Current domain "+doctor_details.specalization);
                ratings.setRating(doctor_details.ratings);
                reviewcount.setText("("+doctor_details.count+" reviews)");
                profile_pic=doctor_details.profile_pic;
                try{
                    Glide.with(DoctorProfile.this)
                            .load(""+profile_pic)
                            .into(profilepic);
                    progressBar.setVisibility(View.VISIBLE);
                }
                catch (Exception e){}
                progressDialog.dismiss();
                progressBar.setVisibility(View.INVISIBLE);
                databaseReference.child("Reviews").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1)
                    {
                        list.clear();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            ReviewDetails reviewDetails = dataSnapshot2.getValue(ReviewDetails.class);
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reviewAdapter = new ReviewAdapter(DoctorProfile.this,list);
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
                UIutils uIutils = new UIutils(DoctorProfile.this);
                uIutils.showPhoto(Uri.parse(profile_pic));
            }
        });
    }
    @SuppressLint("ResourceType")
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