package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class LabRegistration extends AppCompatActivity
{
    de.hdodenhof.circleimageview.CircleImageView Profile,dp;
    public Uri imageuri;
    EditText labname,location,propreitorname,isonumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private UploadTask uploadtask;
    SharedPreferences sharedPreferences;
    StorageReference imageref;
    String uid;
    String labname1,location1,propreitorname1,isonumber1;
    ImageButton labnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_registration);
        Profile=findViewById(R.id.profilelab);
        dp=findViewById(R.id.dplab);
        labname=findViewById(R.id.labnamereg);
        location=findViewById(R.id.locationreg);
        propreitorname=findViewById(R.id.proprietornamereg);
        isonumber=findViewById(R.id.isonumberreg);
        labnext=findViewById(R.id.labnextbt);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        imageref= FirebaseStorage.getInstance().getReference("doctors_profile");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("LaboratoryRegistrations");
        /*SharedPreferences sharedPreferences1 = getSharedPreferences("labordoc",MODE_PRIVATE);
        String checker = sharedPreferences1.getString("prefs","");*/
        /*if(user!=null)
        {
            startActivity(new Intent(LabRegistration.this,LabTestInfo.class));
        }*/
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });
        labnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String androiid= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                if(isValid())
                {
                    StorageReference ref = imageref.child(androiid).child(getextension(imageuri)+"#");
                    if(imageuri==null)
                    {
                        imageuri= Uri.parse("android.resource://com.example.ehospital/drawable/noimg");
                    }
                    uploadtask = ref.putFile(imageuri);
                    uploadtask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LabRegistration.this,"failed",Toast.LENGTH_LONG).show();

                        }
                    }).addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(LabRegistration.this,"success" ,Toast.LENGTH_LONG).show();
                        imageref.child(androiid).child(getextension(imageuri)+"#").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String profile_pic=task.getResult().toString();
                                SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                                SharedPreferences.Editor editor1=sharedPreferences.edit();
                                editor1.putString("uid",uid);
                                editor1.commit();
                                LaboratoryRegistrationDetails laboratoryRegistrationDetails=new LaboratoryRegistrationDetails(labname1,location1,propreitorname1,isonumber1,"lab",profile_pic);
                                databaseReference.child(uid).setValue(laboratoryRegistrationDetails);
                                SharedPreferences.Editor editor2=sharedPreferences.edit();
                                editor2.putString("labname",labname1);
                                editor2.putString("location",location1);
                                editor2.commit();
                                sharedPreferences=getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                //editor.putString("prefs","");
                                editor.putString("prefs","lab");
                                editor.commit();
                                startActivity(new Intent(LabRegistration.this,LabTestInfo.class));
                            }
                        });

                    });
                }
                else
                    Toast.makeText(LabRegistration.this,"No null values!!!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void selectimage()
    {
        Intent intent=new Intent();
        // ivc.setVisibility(View.VISIBLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    boolean isValid()
    {
        boolean i=false;
        labname1=labname.getText().toString();
        location1=location.getText().toString();
        propreitorname1=propreitorname.getText().toString();
        isonumber1=isonumber.getText().toString();
        if(labname1.isEmpty() || location1.isEmpty() || propreitorname1.isEmpty() || isonumber1.isEmpty())
        {
            if(labname1.isEmpty())
                labname.setError("Can't be empty");
            if(location1.isEmpty())
                location.setError("Can't be empty");
            if(propreitorname1.isEmpty())
                propreitorname.setError("Can't be empty");
            if(isonumber1.isEmpty())
                isonumber.setError("Can't be empty");
        }
        else
            i=true;
        return i;
    }
    private String getextension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        if(uri==null)
        {
            uri= Uri.parse("android.resource://com.example.ehospital/drawable/noimg");
            return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
        }
        else
        {
            return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK  && data.getData()!=null)
        {imageuri=data.getData();

            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imageuri);
                Profile.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(LabRegistration.this,"no media selected",Toast.LENGTH_SHORT).show();
        }
    }
}