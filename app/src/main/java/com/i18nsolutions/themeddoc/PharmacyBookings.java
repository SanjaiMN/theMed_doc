package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PharmacyBookings extends AppCompatActivity implements RecyclerInterface
{
    LabBookingAdapter labBookingAdapter;
    RecyclerView recyclerView;
    List<LabPaymentDetails> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    TextView textView;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your Bookings");
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pharmacy_bookings);
        recyclerView=findViewById(R.id.pharmacybookingsrecyclerview);
        progressBar=findViewById(R.id.progressBarpharmacybookings);
        textView=findViewById(R.id.notvpharmbookings);
        progressBar.setVisibility(View.VISIBLE);
        list=new ArrayList<>();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getfromdatabase();
    }
    void getfromdatabase()
    {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Payment Report").child("Pharmacy").child(uid);
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
        labBookingAdapter=new LabBookingAdapter(PharmacyBookings.this,list,PharmacyBookings.this);
        labBookingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(labBookingAdapter.getItemCount()==0)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.INVISIBLE);
            }
        });
        recyclerView.setAdapter(labBookingAdapter);
    }

    @Override
    public void OnButtonClick(int position)
    {
        Intent intent = new Intent(PharmacyBookings.this,PharmacyFullDetails.class);
        intent.putExtra("serialno",list.get(position));
        startActivity(intent);
    }
}