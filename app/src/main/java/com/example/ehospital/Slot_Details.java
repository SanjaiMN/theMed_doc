package com.example.ehospital;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Slot_Details extends AppCompatActivity implements RecyclerInterface{
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ProgressDialog progressdialog;
    List<SlotDetails> list;
    private recyclerAdapter_slot recycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot__details);
        progressdialog = new ProgressDialog(Slot_Details.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        progressdialog.setCancelable(false);
        recyclerView=findViewById(R.id.storerecycler);
        list=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getfromdatabase();

    }
    public void getfromdatabase()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String uid=sharedPreferences.getString("uid","");
        recycleradapter = new recyclerAdapter_slot(getApplicationContext(),this,list);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("slot_Booked").child(uid);
       // System.out.println(">>>>>>>>>>>>>"+databaseReference);
        //Query query=databaseReference.orderByChild("uid").equalTo(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    SlotDetails slotDetails = dataSnapshot1.getValue(SlotDetails.class);
                    list.add(slotDetails);
                }
                recyclerView.setAdapter(recycleradapter);
                recycleradapter.notifyDataSetChanged();
                progressdialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recycleradapter = new recyclerAdapter_slot(getApplicationContext(),this,list);
        recyclerView.setAdapter(recycleradapter);
        recycleradapter.notifyDataSetChanged();
    }

    @Override
    public void OnButtonClick(int position)
    {
        Intent intent = new Intent(getApplicationContext(),SlotFullDetails.class);
        intent.putExtra("slotconfirm",list.get(position));
        startActivity(intent);
    }
}