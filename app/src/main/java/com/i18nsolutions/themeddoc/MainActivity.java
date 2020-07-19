package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "PhoneAuth";
    private String phoneVerificationId;
    private FirebaseAuth fbAuth;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private ProgressDialog progressdialog;
    EditText phoneText,codeText;
    CountryCodePicker ccp;
    String number;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private FancyButton verifyButton;
    private FancyButton sendButton;
    private FancyButton resendButton;
    FirebaseUser user;
    SharedPreferences sharedPreferences;
    String ch;
    LocationManager locationManager;
    String uid;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneText=findViewById(R.id.phoneText);
        verifyButton = findViewById(R.id.verifyButton);
        sendButton = findViewById(R.id.sendButton);
        resendButton = findViewById(R.id.resendButton);
        codeText = findViewById(R.id.codeText);
        ch=phoneText.getText().toString();
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);
        fbAuth = FirebaseAuth.getInstance();
        user= fbAuth.getCurrentUser();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location) {

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {

            }
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            requestPermissions(new String[]{
                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 10);
            return;
        } else {
            configurationbutton();
        }

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configurationbutton();
        }
    }

    void configurationbutton()
    {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    requestPermissions(new String[]{
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 10);
                    return;
                }
                else
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        if(user!=null)
        {
            startActivity(new Intent(MainActivity.this,DoctorOrLabTechnician.class));
        }
    }
    public void sendCode(View view) {
        number = ccp.getFullNumberWithPlus();
        if(number.length()<=3) {
            phoneText.setError("Can't be empty");
        }
        else {
            progressdialog = new ProgressDialog(MainActivity.this);
            progressdialog.setMessage("Please Wait....");
            setUpVerificatonCallbacks();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    verificationCallbacks);
        }
    }
    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token)
                    {
                        progressdialog.show();
                        phoneVerificationId = verificationId;
                        resendToken = token;

                        verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        resendButton.setEnabled(true);
                    }
                };

    }
    public void verifyCode(View view) {

        String code = codeText.getText().toString();
        if(code.isEmpty())
            codeText.setError("Can't be Empty");
        else {
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId,code);
            String checkcode=credential.getSmsCode();
            if(checkcode.equals(code))
                signInWithPhoneAuthCredential(credential);
            else
                Toast.makeText(getApplicationContext(),"Invalid code",Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();
                            String phoneNumber = user.getPhoneNumber();
                            progressdialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, DoctorOrLabTechnician.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(MainActivity.this,"The verification code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void resendCode(View view) {

        number = ccp.getFullNumberWithPlus();
            setUpVerificatonCallbacks();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    verificationCallbacks,
                    resendToken);
        }

}

