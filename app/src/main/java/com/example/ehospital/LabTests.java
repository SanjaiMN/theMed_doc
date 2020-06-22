package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class LabTests extends AppCompatActivity implements  SearchView.OnQueryTextListener
{
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<LabDetails> list,refreshlist;
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
        refreshlist=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        getfromdatabase();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
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
                refreshlist.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                   LabDetails labDetails=dataSnapshot1.getValue(LabDetails.class);
                   list.add(labDetails);
                   refreshlist.add(labDetails);
                   System.out.println(labDetails.labname);
                }
                progressBar.setVisibility(View.INVISIBLE);
                recycleradapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recycleradapter=new RecyclerAdaptorLabTests(this,list);
        recyclerView.setAdapter(recycleradapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText==null)
            recycleradapter.updatelist(list);
        else {
            String userinput = newText.toLowerCase().trim();
            List<LabDetails> newList = new ArrayList<>();
            for (LabDetails string : list) {
                if (string.testname.toLowerCase().contains(userinput)|| string.includestestname.toLowerCase().contains(userinput) || String.valueOf(string.serialno).contains(userinput) || string.nooftest.toLowerCase().contains(userinput) || string.money.contains(userinput))
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