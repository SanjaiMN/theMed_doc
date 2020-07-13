package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SlotAvailability extends AppCompatActivity {

    GridView gridView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_availability);
        String[] slotname={"slot1","slot2","slot3","slot4","slot5","slot6","slot7","slot8",
                "slot9","slot10","slot11","slot12","slot13","slot14"};
        String[] timing={"10:00-10:30","10:30-11:00","11:00-11:30","11:30-12:00","12:00-12:30","12:30-1:00","3:00-3:30","3:30-4:00","4:00-4:30",
        "4:30-5:00","5:00-5:30","5:30-6:00","8:00-8:30","8:30-9:00"};
        System.out.println(slotname.length);
        SlotAvailAdapter grid_base_adapter=new SlotAvailAdapter(SlotAvailability.this,slotname,timing);
        gridView=(GridView)findViewById(R.id.gridslotavail);
        gridView.setAdapter(grid_base_adapter);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Doctor database").child(uid).child("Slots");
        databaseReference1=FirebaseDatabase.getInstance().getReference().child("Doctor database").child(uid).child("Slots");
        gridView.setOnItemClickListener((parent, view, position, id) ->
        {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String check=dataSnapshot.child(slotname[position]).getValue().toString();
                    if(check.equals("false"))
                    {
                        new AlertDialog.Builder(SlotAvailability.this)
                                .setTitle("Make your slot true")
                                .setMessage("Are you sure you want to make it as true?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface arg0, int arg1)
                                    {
                                        databaseReference1.child(slotname[position]).setValue(true);
                                        SlotAvailAdapter grid_base_adapter=new SlotAvailAdapter(SlotAvailability.this,slotname,timing);
                                        gridView=(GridView)findViewById(R.id.gridslotavail);
                                        gridView.setAdapter(grid_base_adapter);
                                    }
                                }).create().show();
                    }
                    else
                    {
                        new AlertDialog.Builder(SlotAvailability.this)
                                .setTitle("Make your slot false")
                                .setMessage("Are you sure you want to make it as false?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface arg0, int arg1)
                                    {
                                        databaseReference1.child(slotname[position]).setValue(false);
                                        SlotAvailAdapter grid_base_adapter=new SlotAvailAdapter(SlotAvailability.this,slotname,timing);
                                        gridView=(GridView)findViewById(R.id.gridslotavail);
                                        gridView.setAdapter(grid_base_adapter);
                                    }
                                }).create().show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }
}