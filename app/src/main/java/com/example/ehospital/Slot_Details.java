package com.example.ehospital;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Slot_Details extends AppCompatActivity implements RecyclerInterface, SearchView.OnQueryTextListener {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<SlotDetails> list,refreshlist;
    private recyclerAdapter_slot recycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot__details);
        progressBar=findViewById(R.id.progressBardoctor);
        recyclerView=findViewById(R.id.storerecycler);
        list=new ArrayList<>();
        refreshlist=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String uid=sharedPreferences.getString("uid","");
        recycleradapter = new recyclerAdapter_slot(getApplicationContext(),this,list);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("slot_Booked").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                refreshlist.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    SlotDetails slotDetails = dataSnapshot1.getValue(SlotDetails.class);
                    list.add(slotDetails);
                    refreshlist.add(slotDetails);
                }
                recyclerView.setAdapter(recycleradapter);
                progressBar.setVisibility(View.INVISIBLE);
                recycleradapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recycleradapter = new recyclerAdapter_slot(getApplicationContext(),this,list);
        recyclerView.setAdapter(recycleradapter);
        recycleradapter.notifyDataSetChanged();
    }

    @Override
    public void OnButtonClick(int position)
    {
        Intent intent = new Intent(getApplicationContext(),SlotFullDetails.class);
        intent.putExtra("slotconfirm",list.get(position));
        startActivity(intent);
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
            List<SlotDetails> newList = new ArrayList<>();
            for (SlotDetails string : list) {
                if (string.name.toLowerCase().contains(userinput))
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