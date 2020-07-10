//package com.example.ehospital;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.Manifest;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//
//import com.opentok.android.BaseVideoCapturer;
//import com.opentok.android.OpentokError;
//import com.opentok.android.Publisher;
//import com.opentok.android.PublisherKit;
//import com.opentok.android.Session;
//import com.opentok.android.Stream;
//import com.opentok.android.Subscriber;
//
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
//public class videocall extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {
//    //private static String LOG_TAG=Video_call.class.getSimpleName();
//    private static final int RC_SETTINGS=123;
//    private Session session;
//    private FrameLayout publisher_container,subscriber_container;
//    private Publisher publisher;
//    private Subscriber subscriber;
//    Button endcall,switchcamera,mute;
//    int click=1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_videocall);
//        requestPermissions();
//        publisher_container=findViewById(R.id.publisher_container);
//        subscriber_container=findViewById(R.id.subscriber_container);
//        endcall=findViewById(R.id.endcall);
//        switchcamera=findViewById(R.id.switchcamera);
//        mute=findViewById(R.id.mute);
//        endcall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                session.disconnect();
//                finish();
//            }
//        });
//        switchcamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BaseVideoCapturer capturer = publisher.getCapturer();
//                if (capturer.isCaptureStarted()) {
//                    capturer.stopCapture();
//                }
//                capturer.destroy();
//                publisher.cycleCamera();
//                capturer.init();
//                capturer.startCapture();
//            }
//        });
//        mute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(click%2==0)
//                {
//                    publisher.setPublishAudio(true);
//                    Toast.makeText(getApplicationContext(),"audio on",Toast.LENGTH_SHORT).show();
//
//                }else {
//                    publisher.setPublishAudio(false);
//                    Toast.makeText(getApplicationContext(),"audio off",Toast.LENGTH_SHORT).show();
//                }
//                click++;
//
//
//
//            }
//        });
//
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
//
//    }
//    @AfterPermissionGranted(RC_SETTINGS)
//    private void requestPermissions()
//    {
//        String[] pers={Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
//        if(EasyPermissions.hasPermissions(this,pers))
//        {
//            String SESSION_ID = "2_MX40Njc2MTA3Mn5-MTU5Mzc3Mzk2MDU1MX5SMVViV2g0eHBGTUtTRktpTnBWQVZseUl-fg";
//            String API_KEY = "46761072";
//            session=new Session.Builder(this, API_KEY, SESSION_ID).build();
//            session.setSessionListener(this);
//            String TOKEN = "T1==cGFydG5lcl9pZD00Njc2MTA3MiZzaWc9NjAyNThkNDU1M2QzMDJkYzBmNTFkYzEwYWNlNjM2YTE0NjkxM2EzNTpzZXNzaW9uX2lkPTJfTVg0ME5qYzJNVEEzTW41LU1UVTVNemMzTXprMk1EVTFNWDVTTVZWaVYyZzBlSEJHVFV0VFJrdHBUbkJXUVZac2VVbC1mZyZjcmVhdGVfdGltZT0xNTkzNzczOTg0Jm5vbmNlPTAuNjc5OTExMjc2ODgyMzYzJnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE1OTM3Nzc1ODImaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";
//            session.connect(TOKEN);
//        }
//        else
//        {
//            EasyPermissions.requestPermissions(this,"NEEDS to access camera and audio",RC_SETTINGS,pers);
//        }
//    }
//
//
//    @Override
//    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
//
//    }
//
//    @Override
//    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
//
//    }
//
//    @Override
//    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
//
//    }
//
//    @Override
//    public void onConnected(Session session) {
//        publisher=new Publisher.Builder(this).build();
//        publisher.setPublisherListener(this);
//        publisher_container.addView(publisher.getView());
//        session.publish(publisher);
//
//    }
//
//    @Override
//    public void onDisconnected(Session session) {
//
//    }
//
//    @Override
//    public void onStreamReceived(Session session, Stream stream) {
//        if(subscriber==null)
//        {
//            subscriber=new Subscriber.Builder(this,stream).build();
//            session.subscribe(subscriber);
//            subscriber_container.addView(subscriber.getView());
//        }
//
//    }
//
//    @Override
//    public void onStreamDropped(Session session, Stream stream) {
//        if(subscriber!=null)
//        {
//            subscriber=null;
//            subscriber_container.removeAllViews();
//        }
//
//    }
//
//    @Override
//    public void onError(Session session, OpentokError opentokError) {
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//}
