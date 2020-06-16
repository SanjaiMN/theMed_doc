package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LabTestInfo extends AppCompatActivity
{
    EditText labtestname, nooftest, subtestname, money,serialno;
    String labtestname1, nooftest1, subtestname1, labname1, money1, city1,uidtest,serialno1;
    Button upload;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_info);
        labtestname = findViewById(R.id.labtestname);
        serialno=findViewById(R.id.serialnumberlab);
        nooftest = findViewById(R.id.nooftest);
        subtestname = findViewById(R.id.subtestname);
        money = findViewById(R.id.amount);
        upload = findViewById(R.id.upload);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        uidtest=FirebaseAuth.getInstance().getCurrentUser().getUid();
        upload.setOnClickListener(v -> {
            if(isValid())
            {
                LabDetails labDetails = new LabDetails(Integer.parseInt(serialno1),labtestname1, labname1, nooftest1, subtestname1, money1, city1,uidtest);
                databaseReference.child("Labtests").child(city1.toLowerCase()).child(uidtest).child(serialno1).setValue(labDetails);
                Toast.makeText(LabTestInfo.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                clearedtext();
            }
            else
                Toast.makeText(LabTestInfo.this, "No null values!!!", Toast.LENGTH_SHORT).show();
        });
    }
    void clearedtext()
    {
        labtestname.setText("");
        nooftest.setText("");
        subtestname.setText("");
        money.setText("");
        serialno.setText("");
    }
    boolean  isValid()
    {
        boolean i=false;
            labtestname1 = labtestname.getText().toString();
            subtestname1 = subtestname.getText().toString();
            nooftest1 = nooftest.getText().toString();
            money1 = money.getText().toString();
            serialno1=serialno.getText().toString();
            SharedPreferences sharedPreferences5=getSharedPreferences("MyPrefs",MODE_PRIVATE);
            labname1=sharedPreferences5.getString("labname","");
            city1=sharedPreferences5.getString("location","");
            if(labtestname1.isEmpty() || subtestname1.isEmpty()  || nooftest1.isEmpty() || money1.isEmpty() || serialno1.isEmpty())
            {
                if(labtestname1.isEmpty())
                    labtestname.setError("Can't be empty");
                if(subtestname1.isEmpty())
                    subtestname.setError("Can't be empty");
                if(nooftest1.isEmpty())
                    nooftest.setError("Can't be empty");
                if(money1.isEmpty())
                    money.setError("Can't be empty");
                if(serialno1.isEmpty())
                    serialno.setError("Can't be empty");
            }
            else
                i=true;
        return i;
    }
}