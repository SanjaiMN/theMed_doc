package com.i18nsolutions.themeddoc;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Slot_Details extends AppCompatActivity implements RecyclerInterface, SearchView.OnQueryTextListener {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textView;
    TaskAlarmCreator taskAlarmCreator;
    NotificationDetails notificationDetails;
    DBHelper dbHelper;
    String uid;
    List<SlotDetails> list,refreshlist;
    private recyclerAdapter_slot recycleradapter;
    String todaydate;
    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your Patients");
        setContentView(R.layout.activity_slot__details);
        textView=findViewById(R.id.nobookstvslot);
        notificationDetails = new NotificationDetails();
        taskAlarmCreator=new TaskAlarmCreator(notificationDetails,this);
        dbHelper = new DBHelper(this);
        progressBar=findViewById(R.id.progressBardoctor);
        recyclerView=findViewById(R.id.storerecycler);
        Calendar calendar=Calendar.getInstance();
        Date date=calendar.getTime();
         simpleDateFormat=new SimpleDateFormat("EEE, MMM d, yyyy");
        todaydate=simpleDateFormat.format(date);
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
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                    notificationDetails.mode=slotDetails.mode;
                    String str[] = slotDetails.time.split("-");
                    String newtime=str[0];
                    String str2[]=newtime.split(" ");
                    if(str2[1].equals("PM"))
                    {
                        String str3[] = str2[0].split(":");
                        int newtimeint=Integer.parseInt(str3[0])+12;
                        str2[0]=String.valueOf(newtimeint)+":"+str3[1];
                    }
                    System.out.println(str2[0]);
                    notificationDetails.time=str2[0];
                    notificationDetails.date=slotDetails.date;
                    notificationDetails.name=slotDetails.name;
                    notificationDetails.taskid=getUniqueTaskId();

                    try {
                        if (simpleDateFormat.parse(slotDetails.date).equals(simpleDateFormat.parse(todaydate))||simpleDateFormat.parse(slotDetails.date).after(simpleDateFormat.parse(todaydate)))
                        {
                            if(simpleDateFormat.parse(slotDetails.date).equals(simpleDateFormat.parse(todaydate))) {
                                taskAlarmCreator.createAlarm();
                                if (dbHelper.insert(notificationDetails)) {

                                }
                            }
                            list.add(slotDetails);
                            refreshlist.add(slotDetails);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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
        recycleradapter.notifyDataSetChanged();
    }
    private int getUniqueTaskId() {
        SharedPreferences shared=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int val=shared.getInt("taskUniqueId",0);
        SharedPreferences.Editor edit=shared.edit();
        edit.putInt("taskUniqueId",val+1);
        edit.apply();
        return val;
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