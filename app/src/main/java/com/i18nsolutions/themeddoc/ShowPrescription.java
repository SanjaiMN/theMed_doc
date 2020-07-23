package com.i18nsolutions.themeddoc;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPrescription extends AppCompatActivity
{
    PrescriptionAdapter prescriptionAdapter;
    RecyclerView prescriptionrecycler;
    List<PrescriptionDetails> list;
    ProgressBar progressBar;
    TextView textView;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Patient's Prescription");
        setContentView(R.layout.activity_show_prescription);
        prescriptionrecycler=findViewById(R.id.prescriptionrecycler);
        list=new ArrayList<>();
        Intent intent =getIntent();
        progressBar=findViewById(R.id.prescriptionpb);
        progressBar.setVisibility(View.VISIBLE);
        textView=findViewById(R.id.tvnoprescription);
        uid=intent.getExtras().get("uidpatient").toString();
        prescriptionrecycler.setLayoutManager(new GridLayoutManager(this, 2));
        prescriptionrecycler.setHasFixedSize(true);
        prescriptionAdapter=new PrescriptionAdapter(list,this);
        prescriptionrecycler.setAdapter(prescriptionAdapter);
        getdatafromdatabase();
        prescriptionrecycler.setHasFixedSize(true);
        prescriptionrecycler.setAdapter(prescriptionAdapter);
        prescriptionAdapter.notifyDataSetChanged();

    }

    private void getdatafromdatabase() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("Prescription").child(uid);
        DatabaseReference finalDatabaseReference = databaseReference;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                progressBar.setVisibility(View.INVISIBLE);
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String key=dataSnapshot.getKey();
                    System.out.println(key);
                    finalDatabaseReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1)
                        {
                            PrescriptionDetails prescriptionDetails = snapshot1.getValue(PrescriptionDetails.class);
                            list.add(prescriptionDetails);
                            prescriptionrecycler.setHasFixedSize(true);
                            prescriptionAdapter = new PrescriptionAdapter(list,ShowPrescription.this);
                            prescriptionrecycler.setAdapter(prescriptionAdapter);
                            progressBar.setVisibility(View.GONE);
                            prescriptionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                @Override
                                public void onChanged() {
                                    super.onChanged();
                                    if(prescriptionAdapter.getItemCount()==0)
                                        textView.setVisibility(View.VISIBLE);
                                    else
                                        textView.setVisibility(View.INVISIBLE);
                                }
                            });
                            prescriptionAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    prescriptionAdapter.notifyDataSetChanged();
                    prescriptionAdapter = new PrescriptionAdapter(list,ShowPrescription.this);
                    prescriptionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            if(prescriptionAdapter.getItemCount()==0)
                                textView.setVisibility(View.VISIBLE);
                            else
                                textView.setVisibility(View.INVISIBLE);
                        }
                    });
                    prescriptionrecycler.setAdapter(prescriptionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        prescriptionAdapter = new PrescriptionAdapter(list,this);
//        prescriptionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                if(prescriptionAdapter.getItemCount()==0)
//                    textView.setVisibility(View.VISIBLE);
//                else
//                    textView.setVisibility(View.INVISIBLE);
//            }
//        });
//        prescriptionrecycler.setAdapter(prescriptionAdapter);
    }
}