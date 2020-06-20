package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LabTests extends AppCompatActivity
{
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<LabDetails> list;
    String uid;
    private RecyclerAdaptorLabTests recycleradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_tests);
        progressBar=findViewById(R.id.progressBarlab);
        recyclerView=findViewById(R.id.recyclerviewlabtests);
        list=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        getfromdatabase();
    }
    public void getfromdatabase()
    {
        progressBar.setVisibility(View.VISIBLE);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Labtests").child("salem").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                   LabDetails labDetails=dataSnapshot1.getValue(LabDetails.class);
                   list.add(labDetails);
                   System.out.println(labDetails.labname);
                }
                progressBar.setVisibility(View.INVISIBLE);
                recycleradapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recycleradapter=recycleradapter = new RecyclerAdaptorLabTests(this,list);
        recyclerView.setAdapter(recycleradapter);
    }
}