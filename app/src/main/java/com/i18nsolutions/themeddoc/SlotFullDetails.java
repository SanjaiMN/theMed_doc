package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import mehdi.sakout.fancybuttons.FancyButton;

public class SlotFullDetails extends AppCompatActivity
{
    TextView name,date,time,shortdesc;
    FancyButton confirmation,cancel,reports,showprescription;
    FirebaseDatabase firebaseDatabase;
    FancyButton prescription;
    Uri imageuri;
    DatabaseReference databaseReference;
    String uid,patientuid,uidcheck,mode,doctorname;
    ImageButton letschat,videocall;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_full_details);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        name=findViewById(R.id.namefd);
        date=findViewById(R.id.datefd);
        time=findViewById(R.id.timefd);
        letschat=findViewById(R.id.letschat);
        shortdesc=findViewById(R.id.shortdescription);
        confirmation=findViewById(R.id.confirmbuttonfd);
        cancel=findViewById(R.id.cancelbutton);
        showprescription=findViewById(R.id.showprescription);
        reports=findViewById(R.id.healthreports);
        videocall=findViewById(R.id.videocall);
        prescription=findViewById(R.id.prescription);
        Intent intent=getIntent();
        SlotDetails slotDetails = intent.getParcelableExtra("slotconfirm");
        name.append(slotDetails.name);
        date.append(slotDetails.date);
        time.append(slotDetails.time);
        patientuid=slotDetails.puid;
        mode=slotDetails.mode;
        if(!slotDetails.payment)
        {
            videocall.setVisibility(View.INVISIBLE);
            letschat.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(mode.equals("Video call"))
            {
                videocall.setVisibility(View.VISIBLE);
                letschat.setVisibility(View.INVISIBLE);
            }
            if(mode.equals("Chat"))
            {
                letschat.setVisibility(View.VISIBLE);
                videocall.setVisibility(View.INVISIBLE);
            }
        }
        shortdesc.append(slotDetails.shortdescription);
        if(slotDetails.confirmation) {
            confirmation.setEnabled(false);
            cancel.setEnabled(false);
            confirmation.setBackgroundColor(Color.GRAY);
            cancel.setBackgroundColor(Color.GRAY);
        }
        if(slotDetails.cancelled) {
            confirmation.setEnabled(false);
            cancel.setEnabled(false);
            confirmation.setBackgroundColor(Color.GRAY);
            cancel.setBackgroundColor(Color.GRAY);
        }
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        uidcheck=slotDetails.puid;
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("slot_Booked").child(uid);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    slotDetails.confirmation = true;
                    slotDetails.duid = uid;
                    cancel.setEnabled(false);
                    cancel.setBackgroundColor(Color.GRAY);
                    databaseReference.child(uidcheck).setValue(slotDetails);
                    confirmation.setBackgroundColor(Color.GREEN);
                    finish();
            }
        });
        letschat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SlotFullDetails.this,ChatActivity.class);
                intent1.putExtra("patuid",patientuid);
                startActivity(intent1);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                slotDetails.cancelled=true;
                cancel.setBackgroundColor(Color.RED);
                confirmation.setEnabled(false);
                confirmation.setBackgroundColor(Color.GRAY);
                databaseReference.child(uidcheck).setValue(slotDetails);
                finish();
            }
        });
        reports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent(SlotFullDetails.this,HealthReports.class);
                intent1.putExtra("healthreport",patientuid);
                startActivity(intent1);
            }
        });
        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageuri = Uri.fromFile(new File(new File(getExternalCacheDir().getAbsolutePath()) + ("/" + time + ".jpg")));
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(intent, 1);
            }
        });
        showprescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1=new Intent(SlotFullDetails.this,ShowPrescription.class);
                intent1.putExtra("uidpatient",uidcheck);
                startActivity(intent1);
            }
        });
        videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                try {
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(new URL("https://meet.jit.si"))
                            .setRoom(uid)
                            .setAudioMuted(false)
                            .setVideoMuted(false)
                            .setAudioOnly(false)
                            .setWelcomePageEnabled(false)
                            .setFeatureFlag("add-people.enabled" ,false)
                            .setFeatureFlag("live-streaming.enabled" ,false)
                            .setFeatureFlag("invite.enabled",false )
                            .setFeatureFlag("meeting-name.enabled",false )
                            .setFeatureFlag("meeting-password.enabled",false )
                            .setFeatureFlag("raise-hand.enabled",false )
                            .setFeatureFlag("recording.enabled",false )
                            .setFeatureFlag("tile-view.enabled",false )
                            .build();
                    JitsiMeetActivity.launch(SlotFullDetails.this, options);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                //use imageUri here to access the image

//                Bundle extras = data.getExtras();

                Log.e("URI",imageuri.toString());
                sendaudiotodatabase();
//                Bitmap bmp = (Bitmap) extras.get("data.toString());

                // here you will get the image as bitmap


            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
            }
        }
    }
    private void sendaudiotodatabase() {
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Audios").child(""+System.currentTimeMillis());
        storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String url=task.getResult().toString();
                        System.out.println(url);
                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Prescription").child(uidcheck).push();
                        DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference().child("Doctor database").child(uid);
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                doctorname=dataSnapshot.child("name").getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        PrescriptionDetails prescriptionDetails = new PrescriptionDetails("sanjai",url);
                        databaseReference.setValue(prescriptionDetails);
                    }
                });
            }
        });
    }
}