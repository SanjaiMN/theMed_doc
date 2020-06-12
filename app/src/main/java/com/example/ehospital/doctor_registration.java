package com.example.ehospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class doctor_registration extends AppCompatActivity{
    de.hdodenhof.circleimageview.CircleImageView Profile,dp;
    public Uri imageuri;
    RadioGroup rg;
    RadioButton gender;
    EditText nameet;
    EditText ageet;
    EditText workinget,email;
    String gender1,specalization;
    int id;
    String name,age,working_in,mail;
    ImageButton nextbt;
    ProgressDialog pd;
    StorageReference imageref;
    private UploadTask uploadtask;
    FirebaseUser user;
    FirebaseAuth fbAuth;
    String uid;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);
        Profile=findViewById(R.id.profile);
        dp=findViewById(R.id.dp);
        nameet=findViewById(R.id.doctorname);
        email=findViewById(R.id.emaildoctor);
        ageet=findViewById(R.id.agedoctor);
        workinget=findViewById(R.id.workingindoctor);
        nextbt=findViewById(R.id.labnextbt);
        rg=findViewById(R.id.radiogroup);
        name=nameet.getText().toString();
        age=ageet.getText().toString();
        working_in=workinget.getText().toString();
        fbAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageref= FirebaseStorage.getInstance().getReference("doctors_profile");
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("uid",uid);
        editor.apply();
       // assert session != null;
      //  String sessionId = session.getSessionId();

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });
        nextbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    pd.setMessage("Loading...");
                    pd.show();
                    sendtodatabase();
            }
        });
        user= fbAuth.getCurrentUser();
       // DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        /*SharedPreferences sharedPreferences1 = getSharedPreferences("labordoc",MODE_PRIVATE);
        String checker = sharedPreferences1.getString("prefs","");
        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String checkeruid = sharedPreferences2.getString("uid","");*/
        /*if(user!=null )
        {
            startActivity(new Intent(getApplicationContext(),profile.class));
        }*/

    }
    public void selectimage()
    {
        Intent intent=new Intent();
       // ivc.setVisibility(View.VISIBLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    public void sendtodatabase()
    {
        id=rg.getCheckedRadioButtonId();
        gender=findViewById(id);
        final String androiid= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        if(valid())
        {
            if(name.length()>1&&Integer.parseInt(age)>22&&working_in.length()>1)
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
                        Toast.makeText(doctor_registration.this,"failed",Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(doctor_registration.this,"success" ,Toast.LENGTH_LONG).show();
                    imageref.child(androiid).child(getextension(imageuri)+"#").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String profile_pic=task.getResult().toString();
                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference=firebaseDatabase.getReference("Doctor database");
                            name=nameet.getText().toString();
                            age=ageet.getText().toString();
                            working_in=workinget.getText().toString();
                            uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                            String uid=sharedPreferences.getString("uid","");
                            SharedPreferences sharedPreferences5=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                            specalization=sharedPreferences5.getString("category_selected","");
                            /*
                            String status=sharedPreferences.getString("App_state","");
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("name",name);
                            editor.putString("specalization",specalization);
                            editor.putString("Request",Request);
                            editor.apply();*/
                            String sessionId="no";
                            String tokenid="no";
                            String Request="no";
                            sharedPreferences=getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1=sharedPreferences.edit();
                            editor1.putString("prefs","");
                            editor1.putString("prefs","doctor");
                            editor1.commit();
                            doctor_details doctor_details=new doctor_details(name,mail,gender1,specalization,working_in,age,profile_pic,sessionId,tokenid,uid,Request,"doctor");
                            databaseReference.child(uid).setValue(doctor_details);
                            pd.dismiss();
                            startActivity(new Intent(getApplicationContext(), profile.class));
                        }
                    });

                });
            }
            else
            {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Enter the details correctly",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            pd.dismiss();
            Toast.makeText(getApplicationContext(),"Empty values!!!",Toast.LENGTH_SHORT).show();
        }
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
                dp.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(doctor_registration.this,"no media selected",Toast.LENGTH_SHORT).show();
        }
    }
    boolean valid()
    {
        boolean i=false;
        try {
            name = nameet.getText().toString();
            age = ageet.getText().toString();
            working_in = workinget.getText().toString();
            mail = email.getText().toString();
            gender1 = gender.getText().toString();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Empty values!!!", Toast.LENGTH_SHORT).show();
        }
        if(name.isEmpty() || age.isEmpty() || working_in.isEmpty() || mail.isEmpty())
        {
            if(name.isEmpty())
                nameet.setError("Can't be empty");
            if(age.isEmpty())
                ageet.setError("Can't be empty");
            if(working_in.isEmpty())
                workinget.setError("Can't be empty");
            if(mail.isEmpty())
                email.setError("Can't be empty");
        }
        else
            i=true;
        return i;
    }
}
