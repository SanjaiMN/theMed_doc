package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MedicineLists extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    DatabaseReference databaseReference,databaseReference2;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<MedicineDetails> list,refreshlist;
    String uid,city1;
    TextView textView;
    RecyclerAdaptorPharmacy recycleradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Your Medicines");
        setContentView(R.layout.activity_medicine_lists);
        progressBar=findViewById(R.id.progressBar2);
        textView=findViewById(R.id.notvmedicinelist);
        recyclerView=findViewById(R.id.recyclerviewpharmacy);
        Intent intent = getIntent();
        city1=intent.getStringExtra("cityname");
        list=new ArrayList<>();
        refreshlist=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        getfromdatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        getMenuInflater().inflate(R.menu.cleardata,menu);
        MenuItem item=menu.findItem(R.id.search);
        MenuItem clear=menu.findItem(R.id.clearall);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.clearall:
            {
                new AlertDialog.Builder(this)
                        .setTitle("Really Delete?")
                        .setMessage("Are you sure you want to delete?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference().child("MedicineDetails").child(city1.toLowerCase()).child(uid);
                                databaseReference1.removeValue();
                            }
                        }).create().show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getfromdatabase()
    {
        progressBar.setVisibility(VISIBLE);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("MedicineDetails").child(city1.toLowerCase()).child(uid);
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                refreshlist.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    MedicineDetails medicineDetails = dataSnapshot1.getValue(MedicineDetails.class);
                    list.add(medicineDetails);
                    refreshlist.add(medicineDetails);
                }
                progressBar.setVisibility(INVISIBLE);
                recycleradapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recycleradapter = new RecyclerAdaptorPharmacy(this,list);
        recycleradapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(recycleradapter.getItemCount()==0)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.INVISIBLE);
            }
        });
        recyclerView.setAdapter(recycleradapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if(newText==null)
            recycleradapter.updatelist(list);
        else {
            String userinput = newText.toLowerCase().trim();
            List<MedicineDetails> newList = new ArrayList<>();
            for (MedicineDetails string : list) {
                if (string.medicinename.toLowerCase().contains(userinput)|| string.category.toLowerCase().contains(userinput) || String.valueOf(string.serialno).contains(userinput) || string.medicinecategory.toLowerCase().contains(userinput) || string.money.contains(userinput) || (string.outofstock+"").contains(userinput))
                {
                    newList.add(string);
                }
            }
            recycleradapter.updatelist(newList);
            refreshlist.clear();
            refreshlist.addAll(newList);
        }
        return false;
    }

}