package com.i18nsolutions.themeddoc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatActivity extends AppCompatActivity
{
    String sentby,timestamp,messagetext="";
    EditText messagesend;
    MediaRecorder recorder = new MediaRecorder();
    ImageButton send_message;
    private RecyclerView mRecyclerView;
    String patuid,uid;
    boolean keyboardUp = false;
    boolean isRunning = false;
    Uri imageuri;
    Vibrator vibrator;
    List<MessageModel> list,newlist;
    Handler handler;
    boolean isRotate=true;
    private String fileName=null;
    FloatingActionButton floatingActionButton,imagefab,micfab;
    private MessageAdapter mAdapter;
    LinearLayoutManager layoutManager;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewmessage);
        messagesend=findViewById(R.id.messagesend);
        send_message=findViewById(R.id.send_message);
        floatingActionButton=findViewById(R.id.fabattach);
        imagefab=findViewById(R.id.imagefab);
        micfab=findViewById(R.id.micfab);
        Fabanimation.init(imagefab);
        Fabanimation.init(micfab);
        vibrator = (Vibrator) ChatActivity.this.getSystemService(VIBRATOR_SERVICE);
        boolean isMountend = Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED);
        imagefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });
        list=new ArrayList<>();
        newlist=new ArrayList<>();
        micfab.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    micfab.animate().scaleX(1.9f).scaleY(1.9f).setDuration(0);
                    vibrator.vibrate(500);
                    try {
                        startRecording();
                    }
                    catch (Exception e)
                    {
                    }
                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    micfab.animate().scaleX(1.0f).scaleY(1.0f).setDuration(0);
                    stopRecording();
                    Toast.makeText(ChatActivity.this,"Stopped",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isRotate= Fabanimation.rotateFab(view,isRotate);
                if(isRotate){

                    messagesend.setVisibility(View.INVISIBLE);
                    send_message.setVisibility(View.INVISIBLE);
                    Fabanimation.showIn(imagefab);
                    Fabanimation.showIn(micfab);
                    isRotate=false;
                }else{
                    messagesend.setVisibility(View.VISIBLE);
                    send_message.setVisibility(View.VISIBLE);
                    Fabanimation.showOut(imagefab);
                    Fabanimation.showOut(micfab);
                    isRotate=true;
                }
            }
        });
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent=getIntent();
        patuid=intent.getExtras().get("patuid").toString();
        getfromdatabase();
        list.clear();
        handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                layoutManager=new LinearLayoutManager(ChatActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.setStackFromEnd(true);
                layoutManager.setSmoothScrollbarEnabled(true);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                mRecyclerView.setLayoutManager(layoutManager);
                mAdapter = new MessageAdapter( list,ChatActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setItemViewCacheSize(list.size());
                mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        };
        handler.postDelayed(r, 1000);
        messagesend.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(messagesend.getRootView())) {
                    Log.d("keyboard", "keyboard UP");

                    if (keyboardUp == false) {
                        if (list.size() > 0)
                            mRecyclerView.smoothScrollToPosition(list.size() + 1);
                        keyboardUp = true;
                    }

                } else {
                    Log.d("keyboard", "keyboard Down");
                    keyboardUp = false;
                }

            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagetext = messagesend.getText().toString();
                if (!messagetext.isEmpty())
                {
                    SimpleDateFormat s = new SimpleDateFormat("hh:mm");
                    SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
                    timestamp = s.format(new Date());
                    String date = d.format(new Date());
                    sentby = "doctor";
                    sendtodatabase(messagetext, "text");
                    messagesend.getText().clear();
                    if (list.size() > 0) {
                        mAdapter = new MessageAdapter(list, ChatActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.smoothScrollToPosition(list.size() + 1);
                    } else {
                        Toast.makeText(ChatActivity.this, ">>>>>>>>>>>>>>>", Toast.LENGTH_SHORT);
                    }
                }
                else{}
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK  && data.getData()!=null)
        {imageuri=data.getData();
            sendimagetodatabase(imageuri);
        }
        else
        {
            Toast.makeText(ChatActivity.this,"no media selected",Toast.LENGTH_SHORT).show();
        }
    }
    private void sendimagetodatabase(Uri imageuri)
    {
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Imagesmessage").child(""+System.currentTimeMillis());
        storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String url=task.getResult().toString();
                        sendtodatabase(url,"photo");
                    }
                });
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
    public void sendtodatabase(String message,String type)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Messages").child(uid).child(patuid).push();
        MessageModel message_modal = new MessageModel(message, timestamp, "doctor",type);
        databaseReference.setValue(message_modal);
    }

    private void startRecording() {
        try {
            fileName = getExternalCacheDir().getAbsolutePath();
            fileName += "/"+System.currentTimeMillis()+".mp3";
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setOutputFile(fileName);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
            Toast.makeText(ChatActivity.this,"Recording Started......",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }
    private void stopRecording() {
        try {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            sendaudiotodatabase();
        }
        catch (Exception e){}
    }
    private void sendaudiotodatabase() {
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Audios").child(""+System.currentTimeMillis());
        Uri uri=Uri.fromFile(new File(fileName));
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String url=task.getResult().toString();
                        sendtodatabase(url,"audio");
                        Toast.makeText(ChatActivity.this,"Success",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }
    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }
    public  void getfromdatabase()
    {

        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Messages").child(uid).child(patuid);
        databaseReference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {

                list.clear();
                for(DataSnapshot dataSnapshot1:snapshot1.getChildren()) {
                    String key1=dataSnapshot1.getKey();
//                                System.out.println(key1);
                    DatabaseReference databaseReference3=databaseReference1.child(key1);
//                                System.out.println(databaseReference3);
                    databaseReference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            MessageModel message_modal=snapshot2.getValue(MessageModel.class);
                            list.add(message_modal);
                            mRecyclerView.setHasFixedSize(true);
                            mAdapter=new MessageAdapter(list,ChatActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setItemViewCacheSize(list.size());
                            mAdapter.notifyDataSetChanged();
                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    // Save state
                }
                mRecyclerView.setHasFixedSize(true);
                mAdapter=new MessageAdapter(list,ChatActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setItemViewCacheSize(list.size());
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mAdapter=new MessageAdapter(list,ChatActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemViewCacheSize(list.size());
        mAdapter.notifyDataSetChanged();

    }
}