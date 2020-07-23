package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class LabUploadCsv extends AppCompatActivity
{
    String testname, includestestname,labname;
    String money,nooftest,city,uidlab;
    Uri imageuri;
    FancyButton upload;
    TextView filename,webpage;
    ImageButton attachfiles;
    DatabaseReference databaseReference,databaseReference2;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    List<LabDetails> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Upload tests");
        setContentView(R.layout.activity_lab_upload_csv);
        attachfiles=findViewById(R.id.attachfileslab);
        upload = findViewById(R.id.submitlab);
        filename=findViewById(R.id.filenamelab);
        webpage=findViewById(R.id.webview);
        uidlab=FirebaseAuth.getInstance().getCurrentUser().getUid();
        filename.setText("choose a csv file");
        databaseReference2=FirebaseDatabase.getInstance().getReference().child("LaboratoryRegistrations").child(uidlab);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                city=dataSnapshot.child("location").getValue().toString();
                labname=dataSnapshot.child("Laboratoryname").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        webpage.setMovementMethod(LinkMovementMethod.getInstance());
        webpage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://www.zamzar.com/convert/xls-to-csv/"));
                startActivity(browserIntent);
            }
        });
        attachfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectimage();
            }
        });
    }
    public void selectimage()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK  && data.getData()!=null)
        {
            imageuri=data.getData();
            File file = new File(imageuri.toString());
            filename.setText(file.getName().substring(10));
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zzzzz();
                }
            });
        }
        else
        {
            Toast.makeText(LabUploadCsv.this,"no media selected",Toast.LENGTH_SHORT).show();
        }
    }
    void zzzzz()
    {
        InputStream inputStream = null;
        try
        {
            System.out.println(">>>>>>>>>>>>>>>>>>"+imageuri);
            inputStream = getContentResolver().openInputStream(imageuri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String line="";
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Labtests").child(city.toLowerCase()).child(uidlab);
        try {
            bufferedReader.readLine();
            while ((line=bufferedReader.readLine()) != null)
            {
                String[] tokens = line.split(",");
                LabDetails labDetails = new LabDetails();
                labDetails.serialno=Integer.parseInt(tokens[0]);
                labDetails.testname=tokens[1];
                labDetails.nooftest=tokens[2];
                labDetails.includestestname=tokens[3];
                labDetails.money=tokens[4];
                labDetails.uidtest=uidlab;
                labDetails.labname=labname;
                labDetails.city=city;
                list.add(labDetails);
                databaseReference.child(labDetails.serialno+"").setValue(labDetails);
            }
            Toast.makeText(LabUploadCsv.this,"Added Sucessfully",Toast.LENGTH_LONG).show();
            filename.setText("choose a csv file");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}