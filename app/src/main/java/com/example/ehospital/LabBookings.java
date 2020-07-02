package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LabBookings extends AppCompatActivity implements RecyclerInterface
{
    LabBookingAdapter labBookingAdapter;
    RecyclerView recyclerView;
    List<LabPaymentDetails> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_bookings);
        recyclerView=findViewById(R.id.labbookingsrecyclerview);
        progressBar=findViewById(R.id.progressBarlabbookings);
        progressBar.setVisibility(View.VISIBLE);
        list=new ArrayList<>();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getfromdatabase();
    }
    void getfromdatabase()
    {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Payment Report").child("LabTest").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    LabPaymentDetails labPaymentDetails = dataSnapshot1.getValue(LabPaymentDetails.class);
                    list.add(labPaymentDetails);
                }
                progressBar.setVisibility(View.INVISIBLE);
                labBookingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        labBookingAdapter=new LabBookingAdapter(LabBookings.this,list,LabBookings.this);
        recyclerView.setAdapter(labBookingAdapter);
    }

    @Override
    public void OnButtonClick(int position)
    {
        Intent intent = new Intent(LabBookings.this,LabPaymentFullDetails.class);
        intent.putExtra("serialno",list.get(position));
        startActivity(intent);
    }
}