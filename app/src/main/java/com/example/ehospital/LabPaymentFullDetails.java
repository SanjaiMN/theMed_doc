package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LabPaymentFullDetails extends AppCompatActivity
{
    TextView customername,testname,walkinorhome;
    String customername1,testname1,walkinorhome1;
    double lats,longs;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
    String puid,uid;
    int serialno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_payment_full_details);
        customername=findViewById(R.id.customernamepaylabfull);
        testname=findViewById(R.id.testnamepayfulllab);
        walkinorhome=findViewById(R.id.walkinorhome);
        Intent intent=getIntent();
        LabPaymentDetails labPaymentDetails=intent.getParcelableExtra("serialno");
        puid=labPaymentDetails.puid;
        walkinorhome1=labPaymentDetails.walkinorhome;
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String city = sharedPreferences.getString("location","");
        serialno=labPaymentDetails.serialno;
        databaseReference1=firebaseDatabase.getReference().child("Labtests").child(city).child(uid).child(""+serialno);
        System.out.println(databaseReference1);
        databaseReference=firebaseDatabase.getReference().child("Patient Database").child(puid);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1)
            {
                System.out.println(dataSnapshot1.getKey());
                String labname = dataSnapshot1.child("labname").getValue().toString();
                testname.setText("LabName"+labname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                PatientDetails patientDetails = dataSnapshot.getValue(PatientDetails.class);
                customername.setText("CustomerName"+patientDetails.name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        walkinorhome.setText("Walk in/home"+walkinorhome1);
    }
}