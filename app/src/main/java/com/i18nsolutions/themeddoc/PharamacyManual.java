package com.i18nsolutions.themeddoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mehdi.sakout.fancybuttons.FancyButton;

public class PharamacyManual extends AppCompatActivity
{
    EditText serialno,medicinename,category,medicinecategory,money;
    String serialno1,medicinename1,category1,medicinecategory1,pharmacyname, money1, city1,uidmed;
    FancyButton upload;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Enter Manually");
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pharamacy_manual);
        serialno=findViewById(R.id.serialnopharmacy);
        medicinename = findViewById(R.id.medicinename);
        category = findViewById(R.id.category);
        medicinecategory = findViewById(R.id.medicinecat);
        money = findViewById(R.id.amountmed);
        upload = findViewById(R.id.submitmedmanual);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        uidmed=FirebaseAuth.getInstance().getCurrentUser().getUid();
        upload.setOnClickListener(v -> {
            if(isValid())
            {
                MedicineDetails medicineDetails=new MedicineDetails(Integer.parseInt(serialno1),medicinename1,category1,medicinecategory1,pharmacyname,city1,money1,uidmed,false);
                databaseReference.child("MedicineDetails").child(city1.toLowerCase()).child(uidmed).child(serialno1).setValue(medicineDetails);
                Toast.makeText(PharamacyManual.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                clearedtext();
            }
            else
                Toast.makeText(PharamacyManual.this, "No null values!!!", Toast.LENGTH_SHORT).show();
        });
    }
    void clearedtext()
    {
        medicinename.setText("");
        medicinecategory.setText("");
        category.setText("");
        money.setText("");
        serialno.setText("");
    }
    boolean  isValid()
    {
        boolean i=false;
        medicinename1 = medicinename.getText().toString();
        medicinecategory1 = medicinecategory.getText().toString();
        category1 = category.getText().toString();
        money1 = money.getText().toString();
        serialno1=serialno.getText().toString();
        SharedPreferences sharedPreferences5=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        pharmacyname=sharedPreferences5.getString("labname","");
        city1=sharedPreferences5.getString("location","");
        if(medicinename1.isEmpty() || medicinecategory1.isEmpty()  || category1.isEmpty() || money1.isEmpty() || serialno1.isEmpty())
        {
            if(medicinename1.isEmpty())
                medicinename.setError("Can't be empty");
            if(medicinecategory1.isEmpty())
                medicinecategory.setError("Can't be empty");
            if(category1.isEmpty())
                category.setError("Can't be empty");
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