package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

import mehdi.sakout.fancybuttons.FancyButton;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class doctor_registration extends AppCompatActivity {
    de.hdodenhof.circleimageview.CircleImageView Profile, dp;
    public Uri imageuri;
    RadioGroup rg, rg2;
    RadioButton gender, slotime;
    EditText nameet;
    EditText ageet;
    EditText workinget, email, mobile;
    String gender1, specalization;
    ImageButton addlocation;
    int id, id2;
    double lats, longs;
    String name, age, working_in, mail, slotname1, mobile1;
    ProgressDialog pd;
    StorageReference imageref;
    private UploadTask uploadtask;
    FirebaseUser user;
    FirebaseAuth fbAuth;
    FancyButton nextbt;
    String uid;
    LocationManager locationManager;
    int REQUESTCODELOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_doctor_registration);
        Profile = findViewById(R.id.profile);
        dp = findViewById(R.id.dp);
        nameet = findViewById(R.id.labnamereg);
        email = findViewById(R.id.emaildoctor);
        ageet = findViewById(R.id.proprietornamereg);
        workinget = findViewById(R.id.workingindoctor);
        mobile = findViewById(R.id.mobiledoctor);
        nextbt = findViewById(R.id.labnextbt);
        rg = findViewById(R.id.radiogroup);
        addlocation = findViewById(R.id.addlocationlab);
        rg2 = findViewById(R.id.slotmae);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(doctor_registration.this, "doctor_registration");
        sequence.setConfig(config);
        sequence.addSequenceItem(dp, "Register as doctor,add your profile too", "GOT IT");
        sequence.addSequenceItem(addlocation, "Add your location and you should be in your hospital location for first time of registration", "GOT IT");
        sequence.start();
        name = nameet.getText().toString();
        age = ageet.getText().toString();
        working_in = workinget.getText().toString();
        fbAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageref = FirebaseStorage.getInstance().getReference("doctors_profile");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid);
        editor.apply();
        addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(doctor_registration.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTCODELOCATION);
                } else
                    {
                    new AlertDialog.Builder(doctor_registration.this)
                            .setTitle("Important")
                            .setMessage("Add your Hospital location")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface arg0, int arg1)
                                {
                                    getCurrentLocation();
                                    addlocation.setBackgroundColor(Color.GREEN);
                                    Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_SHORT).show();
                                    arg0.cancel();
                                }
                            }).create().show();
                }
            }
        });
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
        user = fbAuth.getCurrentUser();
    }

    public void selectimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void sendtodatabase() {
        id = rg.getCheckedRadioButtonId();
        gender = findViewById(id);
        id2 = rg2.getCheckedRadioButtonId();
        slotime = findViewById(id2);
        final String androiid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (valid()) {
            if (name.length() > 1 && Integer.parseInt(age) > 24 && working_in.length() > 1) {
                StorageReference ref = imageref.child(androiid).child(getextension(imageuri) + "#");
                if (imageuri == null) {
                    imageuri = Uri.parse("android.resource://com.i18nsolutions.themeddoc/drawable/noimg");
                }
                uploadtask = ref.putFile(imageuri);
                uploadtask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(doctor_registration.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(doctor_registration.this, "success", Toast.LENGTH_SHORT).show();
                    imageref.child(androiid).child(getextension(imageuri) + "#").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String profile_pic = task.getResult().toString();
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Doctor database");
                            name = nameet.getText().toString();
                            age = ageet.getText().toString();
                            working_in = workinget.getText().toString();
                            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String uid1 = sharedPreferences.getString("uid", "");
                            SharedPreferences sharedPreferences5 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            specalization = sharedPreferences5.getString("category_selected", "");
                            sharedPreferences = getSharedPreferences("labordoc", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putString("prefs", "");
                            editor1.putString("prefs", "doctor");
                            editor1.commit();
                            doctor_details doctor_details = new doctor_details(name, mail, mobile1, gender1, specalization, working_in, age, profile_pic, uid, false, "doctor", slotname1, 1f, lats, longs, 0, "0");
                            databaseReference.child(uid).setValue(doctor_details);
                            pd.dismiss();
                            startActivity(new Intent(getApplicationContext(), profile.class));
                        }
                    });

                });
            } else {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Enter the details correctly", Toast.LENGTH_SHORT).show();
            }
        } else {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Empty values!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getextension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if (uri == null) {
            uri = Uri.parse("android.resource://com.example.ehospital/drawable/noimg");
            return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
        } else {
            return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            imageuri = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imageuri);
                dp.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(doctor_registration.this, "no media selected", Toast.LENGTH_SHORT).show();
        }
    }

    boolean valid() {
        boolean i = false;
        try {
            name = nameet.getText().toString();
            age = ageet.getText().toString();
            working_in = workinget.getText().toString();
            mail = email.getText().toString();
            mobile1 = mobile.getText().toString();
            gender1 = gender.getText().toString();
            slotname1 = slotime.getText().toString();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Empty values!!!", Toast.LENGTH_SHORT).show();
        }
        if (name.isEmpty() || age.isEmpty() || working_in.isEmpty() || mail.isEmpty() || lats == 0.0 && longs == 0.0 || slotname1.isEmpty() || mobile1.isEmpty()) {
            if (name.isEmpty())
                nameet.setError("Can't be empty");
            if (age.isEmpty())
                ageet.setError("Can't be empty");
            if (working_in.isEmpty())
                workinget.setError("Can't be empty");
            if (mail.isEmpty())
                email.setError("Can't be empty");
            if (mobile1.isEmpty())
                mobile.setError("Can't be empty");
            if (lats == 0.0 && longs == 0.0)
                addlocation.setBackgroundColor(Color.RED);
        } else
            i = true;
        return i;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODELOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getCurrentLocation();
        }
    }

    void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(doctor_registration.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(doctor_registration.this).removeLocationUpdates(this);
                if(locationResult!=null && locationResult.getLocations().size()>0)
                {
                    int latestlocationindex=locationResult.getLocations().size()-1;
                    lats=locationResult.getLocations().get(latestlocationindex).getLatitude();
                    longs=locationResult.getLocations().get(latestlocationindex).getLongitude();
                }
            }
        }, Looper.getMainLooper());
    }
}
