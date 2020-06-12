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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PharmacyInside extends AppCompatActivity
{
    EditText medicinename,category,medicinecategory,money;
    String medicinename1,category1,medicinecategory1,pharmacyname, money1, city1,uidmed;
    Button upload;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_inside);
        medicinename = findViewById(R.id.medicinename);
        category = findViewById(R.id.category);
        medicinecategory = findViewById(R.id.medicinecat);
        money = findViewById(R.id.amountmed);
        upload = findViewById(R.id.submitmed);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        uidmed=FirebaseAuth.getInstance().getCurrentUser().getUid();
        upload.setOnClickListener(v -> {
            if(isValid())
            {
                MedicineDetails medicineDetails=new MedicineDetails(medicinename1,category1,medicinecategory1,pharmacyname,city1,money1,uidmed);
                databaseReference.child("MedicineDetails").child(city1.toLowerCase()).child(medicinename1.toLowerCase()).child(uidmed).child(category1.toLowerCase()).child(medicinecategory1.toLowerCase()).setValue(medicineDetails);
               // databaseReference.child("Lablistwithtests").child(city1.toLowerCase()).child(labname1).child(labtestname1).setValue(labDetails);
                Toast.makeText(PharmacyInside.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                clearedtext();
            }
            else
                Toast.makeText(PharmacyInside.this, "No null values!!!", Toast.LENGTH_SHORT).show();
        });
    }
    void clearedtext()
    {
        medicinename.setText("");
        medicinecategory.setText("");
        category.setText("");
        money.setText("");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout:
            {
                new AlertDialog.Builder(this)
                        .setTitle("Really logout?")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                sharedPreferences=getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1=sharedPreferences.edit();
                                editor1.putString("prefs","");
                                firebaseAuth= FirebaseAuth.getInstance();
                                firebaseAuth.signOut();
                                finish();
                                Toast.makeText(PharmacyInside.this,"Logout successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PharmacyInside.this,MainActivity.class));
                            }
                        }).create().show();
                break;
            }
            case R.id.profilemenu:
                startActivity(new Intent(PharmacyInside.this,PharmacyProfile.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        moveTaskToBack(true);
                    }
                }).create().show();
    }
    boolean  isValid()
    {
        boolean i=false;
        medicinename1 = medicinename.getText().toString();
        medicinecategory1 = medicinecategory.getText().toString();
        category1 = category.getText().toString();
        money1 = money.getText().toString();
        SharedPreferences sharedPreferences5=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        pharmacyname=sharedPreferences5.getString("labname","");
        city1=sharedPreferences5.getString("location","");
        if(medicinename1.isEmpty() || medicinecategory1.isEmpty()  || category1.isEmpty() || money1.isEmpty())
        {
            if(medicinename1.isEmpty())
                medicinename.setError("Can't be empty");
            if(medicinecategory1.isEmpty())
                medicinecategory.setError("Can't be empty");
            if(category1.isEmpty())
                category.setError("Can't be empty");
            if(money1.isEmpty())
                money.setError("Can't be empty");
        }
        else
            i=true;
        return i;
    }
}