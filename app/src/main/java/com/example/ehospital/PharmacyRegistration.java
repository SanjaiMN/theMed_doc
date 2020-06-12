package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.webkit.MimeTypeMap;
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

public class PharmacyRegistration extends AppCompatActivity
{
    de.hdodenhof.circleimageview.CircleImageView pharmprofile,dppharm;
    public Uri imageuri;
    EditText pharmname,mail,location,propreitorname,licnumber,address,phonenumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private UploadTask uploadtask;
    SharedPreferences sharedPreferences;
    StorageReference imageref;
    String uid;
    String pharmname1,mail1,location1,propreitorname1,licnumber1,address1,phonenumber1;
    ImageButton pharmnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_registration);
        pharmname=findViewById(R.id.labnameregpharm);
        location=findViewById(R.id.locationregpharm);
        propreitorname=findViewById(R.id.proprietornameregpharm);
        licnumber=findViewById(R.id.licensenumberreg);
        address=findViewById(R.id.addressregpharm);
        phonenumber=findViewById(R.id.phoneregpharm);
        pharmprofile=findViewById(R.id.profilepharm);
        dppharm=findViewById(R.id.smallpharmcam);
        pharmnext=findViewById(R.id.pharmnextbt);
        mail=findViewById(R.id.mailmed);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        imageref= FirebaseStorage.getInstance().getReference("Pharmacy_profile");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("PharmacyRegistrations");
        /*SharedPreferences sharedPreferences1 = getSharedPreferences("labordoc",MODE_PRIVATE);
        String checker = sharedPreferences1.getString("prefs","");*/
        /*if(user!=null)
        {
            startActivity(new Intent(LabRegistration.this,LabTestInfo.class));
        }*/
       dppharm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               selectimage();
           }
       });
        pharmnext.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(PharmacyRegistration.this,"failed",Toast.LENGTH_LONG).show();

                        }
                    }).addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(PharmacyRegistration.this,"success" ,Toast.LENGTH_LONG).show();
                        imageref.child(androiid).child(getextension(imageuri)+"#").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String profile_pic=task.getResult().toString();
                                SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                                SharedPreferences.Editor editor1=sharedPreferences.edit();
                                editor1.putString("uid",uid);
                                editor1.commit();
                                PharmacyDetails pharmacyDetails = new PharmacyDetails(pharmname1,mail1,location1,propreitorname1,licnumber1,"pharmacy",profile_pic,address1,phonenumber1,uid,1f);
                                databaseReference.child(uid).setValue(pharmacyDetails);
                                SharedPreferences.Editor editor2=sharedPreferences.edit();
                                editor2.putString("labname",pharmname1);
                                editor2.putString("location",location1);
                                editor2.commit();
                                sharedPreferences=getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                //editor.putString("prefs","");
                                editor.putString("prefs","pharmacy");
                                editor.commit();
                                startActivity(new Intent(PharmacyRegistration.this,PharmacyInside.class));
                            }
                        });

                    });
                }
                else
                    Toast.makeText(PharmacyRegistration.this,"No null values!!!",Toast.LENGTH_SHORT).show();
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
        pharmname1=pharmname.getText().toString();
        mail1=mail.getText().toString();
        location1=location.getText().toString();
        propreitorname1=propreitorname.getText().toString();
        licnumber1=licnumber.getText().toString();
        address1=address.getText().toString();
        phonenumber1=phonenumber.getText().toString();
        if(pharmname1.isEmpty() ||mail1.isEmpty()|| location1.isEmpty() || propreitorname1.isEmpty() || licnumber1.isEmpty() || address1.isEmpty() || phonenumber1.isEmpty())
        {
            if(pharmname1.isEmpty())
                pharmname.setError("Can't be empty");
            if(mail1.isEmpty())
                mail.setError("Can't be empty");
            if(location1.isEmpty())
                location.setError("Can't be empty");
            if(propreitorname1.isEmpty())
                propreitorname.setError("Can't be empty");
            if(licnumber1.isEmpty())
                licnumber.setError("Can't be empty");
            if(address1.isEmpty())
                address.setError("Can't be empty");
            if(phonenumber1.isEmpty())
                phonenumber.setError("Can't be empty");
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
                pharmprofile.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(PharmacyRegistration.this,"no media selected",Toast.LENGTH_SHORT).show();
        }
    }
}