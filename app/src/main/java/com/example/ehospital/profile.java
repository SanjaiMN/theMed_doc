package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class profile extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth fbAuth;
    Button SlotDetails;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       // mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        fbAuth = FirebaseAuth.getInstance();
            // App is running
         /*   SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("App_state","online");
            editor.apply();*/
            SlotDetails=findViewById(R.id.SlotDetails);
            SlotDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),Slot_Details.class));
                }
            });
       /* String uid=sharedPreferences.getString("uid","");
        String name=sharedPreferences.getString("name","");
        String status=sharedPreferences.getString("App_state","");
        String specalization=sharedPreferences.getString("specalization","");
        String working_in=sharedPreferences.getString("working_in","");
        String age=sharedPreferences.getString("age","");
        String profile_pic=sharedPreferences.getString("profile_pic","");
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference("Doctor database");
            Query query=databaseReference.push().orderByChild("uid").equalTo(uid);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        String SessionId = Objects.requireNonNull(userSnapshot.child("SessionId").getValue()).toString();
                        String TokenId = Objects.requireNonNull(userSnapshot.child("TokenId").getValue()).toString();
                       // String Request = Objects.requireNonNull(userSnapshot.child("Request").getValue()).toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("SessionId", SessionId);
                        editor.putString("TokenId", TokenId);
                        editor.apply();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
          String SessionId= sharedPreferences.getString("SessionId","");
           String TokenId= sharedPreferences.getString("TokenId","");
           String Request= sharedPreferences.getString("Request","");
        doctor_details doctor_details = new doctor_details(name, status, specalization, working_in, age, profile_pic, SessionId, TokenId, uid, Request);
        databaseReference.child(uid).setValue(doctor_details);*/
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
               /* SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("App_state","offline");
                editor.apply();
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference("Doctor database");
                String uid=sharedPreferences.getString("uid","");
                String name=sharedPreferences.getString("name","");
                String status=sharedPreferences.getString("App_state","");
                String specalization=sharedPreferences.getString("specalization","");
                String working_in=sharedPreferences.getString("working_in","");
                String age=sharedPreferences.getString("age","");
                String profile_pic=sharedPreferences.getString("profile_pic","");
                String SessionId=sharedPreferences.getString("SessionId","");
                String TokenId=sharedPreferences.getString("TokenId","");
                String Request=sharedPreferences.getString("Request","");
                doctor_details doctor_details=new doctor_details(name,status,specalization,working_in,age,profile_pic,SessionId,TokenId,uid,Request);
                databaseReference.child(uid).setValue(doctor_details);*/
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
                               Toast.makeText(profile.this,"Logout successful",Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(profile.this,MainActivity.class));
                            }
                        }).create().show();
                break;
            case R.id.profilemenu:
                startActivity(new Intent(profile.this,DoctorProfile.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
