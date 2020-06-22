package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class HealthReports extends AppCompatActivity implements RecyclerInterface
{
    RecyclerView recyclerView;
    HealthReportAdapter healthReportAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<pdfuploadmodel> list;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_reports);
        recyclerView=findViewById(R.id.recyclerviewreports);
        recyclerView.setHasFixedSize(true);
        Intent intent = getIntent();
        uid=intent.getExtras().getString("healthreport");
        System.out.println(uid);
        list=new ArrayList<>();
        getdatafromdatabase();
    }
    public void getdatafromdatabase()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("Patient Database").child(uid).child("healthreports");
        DatabaseReference finalDatabaseReference = databaseReference;
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    String key = dataSnapshot1.getKey();
                    pdfuploadmodel pdfuploadmodel = dataSnapshot1.getValue(com.example.ehospital.pdfuploadmodel.class);
                    list.add(pdfuploadmodel);
                }
                healthReportAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        healthReportAdapter = new HealthReportAdapter(this,list,this);
        recyclerView.setAdapter(healthReportAdapter);

    }

    @Override
    public void OnButtonClick(int position)
    {
        String url=list.get(position).url;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "application/pdf");
        startActivity(Intent.createChooser(intent, "Choose an Application:"));
    }
}