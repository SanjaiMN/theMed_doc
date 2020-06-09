package com.example.ehospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SlotFullDetails extends AppCompatActivity
{
    TextView name,date,time,shortdesc;
    Button confirmation,cancel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid,uidcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_full_details);
        name=findViewById(R.id.namefd);
        date=findViewById(R.id.datefd);
        time=findViewById(R.id.timefd);
        shortdesc=findViewById(R.id.shortdescription);
        confirmation=findViewById(R.id.confirmbuttonfd);
        cancel=findViewById(R.id.cancelbutton);
        Intent intent=getIntent();
        SlotDetails slotDetails = intent.getParcelableExtra("slotconfirm");
        name.append(slotDetails.name);
        date.append(slotDetails.date);
        time.append(slotDetails.time);
        shortdesc.append(slotDetails.shortdescription);
        if(slotDetails.confirmation) {
            confirmation.setEnabled(false);
            cancel.setEnabled(false);
            confirmation.setBackgroundColor(Color.GRAY);
            cancel.setBackgroundColor(Color.GRAY);
        }
        if(slotDetails.cancelled) {
            confirmation.setEnabled(false);
            cancel.setEnabled(false);
            confirmation.setBackgroundColor(Color.GRAY);
            cancel.setBackgroundColor(Color.GRAY);
        }
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        uidcheck=slotDetails.puid;
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("slot_Booked").child(uid);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    slotDetails.confirmation = true;
                    slotDetails.duid = uid;
                    cancel.setEnabled(false);
                    cancel.setBackgroundColor(Color.GRAY);
                    databaseReference.child(uidcheck).setValue(slotDetails);
                    confirmation.setBackgroundColor(Color.GREEN);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //databaseReference.child(uidcheck).removeValue();
                slotDetails.cancelled=true;
                cancel.setBackgroundColor(Color.RED);
                confirmation.setEnabled(false);
                confirmation.setBackgroundColor(Color.GRAY);
                databaseReference.child(uidcheck).setValue(slotDetails);
                //startActivity(new Intent(SlotFullDetails.this,Slot_Details.class));
            }
        });
    }
}