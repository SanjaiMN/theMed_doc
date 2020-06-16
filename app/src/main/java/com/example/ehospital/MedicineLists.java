package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedicineLists extends AppCompatActivity
{
    DatabaseReference databaseReference,testreference;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<MedicineDetails> list;
    String uid;
    private RecyclerAdaptorPharmacy recycleradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_lists);
        progressDialog=new ProgressDialog(MedicineLists.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        recyclerView=findViewById(R.id.recyclerviewpharmacy);
        list=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        getfromdatabase();
    }
    public void getfromdatabase()
    {
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("MedicineDetails").child("salem").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    MedicineDetails medicineDetails = dataSnapshot1.getValue(MedicineDetails.class);
                    list.add(medicineDetails);
                }
                recycleradapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();
        recycleradapter=recycleradapter = new RecyclerAdaptorPharmacy(this,list);
        recyclerView.setAdapter(recycleradapter);
    }
}