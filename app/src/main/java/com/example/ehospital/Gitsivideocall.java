package com.example.ehospital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class Gitsivideocall extends JitsiMeetActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gitsivideocall);
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
            JitsiMeetActivity.launch(this, options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {

        }
    }
}