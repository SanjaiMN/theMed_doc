package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LabTests extends AppCompatActivity implements  SearchView.OnQueryTextListener
{
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textView;
    List<LabDetails> list,refreshlist;
    String uid,city1;
    private RecyclerAdaptorLabTests recycleradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Your Tests");
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_lab_tests);
        progressBar=findViewById(R.id.progressBarlab);
        textView=findViewById(R.id.tvnotuploadlab);
        Intent intent = getIntent();
        city1=intent.getStringExtra("citynamelab");
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
        getMenuInflater().inflate(R.menu.cleardata,menu);
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
                                DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference().child("Labtests").child(city1.toLowerCase()).child(uid);
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
        progressBar.setVisibility(View.VISIBLE);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Labtests").child(city1).child(uid);
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