package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LabTests extends AppCompatActivity
{
    DatabaseReference databaseReference,testreference;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    ProgressDialog progressdialog;
    List<LabDetails> list;
    String uid;
    private RecyclerAdaptorLabTests recycleradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_tests);
        progressdialog = new ProgressDialog(LabTests.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        recyclerView=findViewById(R.id.recyclerviewlabtests);
        list=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        getfromdatabase();
    }
    public void getfromdatabase()
    {
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Labtests");
        testreference=databaseReference.child("salem");
        testreference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String key=dataSnapshot1.getKey();
                    DatabaseReference refee=databaseReference.child("salem").child(key).child(uid);
                    refee.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2)
                        {
                            LabDetails labDetails = dataSnapshot2.getValue(LabDetails.class);
                            list.add(labDetails);
                            recycleradapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {

                        }
                    });
                   recycleradapter.notifyDataSetChanged();
                }
                recycleradapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressdialog.dismiss();
        recycleradapter=recycleradapter = new RecyclerAdaptorLabTests(this,list);
        recyclerView.setAdapter(recycleradapter);
    }
}