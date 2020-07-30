package com.i18nsolutions.themeddoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class LabPaymentFullDetails extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mMap;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient2;

    double latitude;
    double longitude;
    double destLat;
    double destLong;
    String api_token, address;
    FancyButton startBT;
    AutoCompleteTextView destinationET;
    LocationRequest mLocationRequest;
    static GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PERMISSION_REQUEST_GPS_CODE = 1234;
    LocationManager locationManager;
    boolean isGPS;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    LatLng origin;
    LatLng dest;
    PolylineOptions lineOptions;
    boolean startTrack = false;
    ArrayList<LatLng> points;


    LatLng previouslatLng;
    Marker m;
    private Location previousLocation;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    //  private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    double lats, longs;
    SupportMapFragment mapFragment;
    android.location.LocationListener locationListener;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    TextView customername, testname, walkinorhome,nomapslab;
    String walkinorhome1,mobilenumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference1,databaseReference3;
    String puid, uid,city;
    String serialno;
    ImageView call;
    FancyButton delivered;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Booking details");
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_lab_payment_full_details);
        mGoogleApiClient2 = new GoogleApiClient.Builder(LabPaymentFullDetails.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            buildGoogleApiClient();

        } else {
            //Request Location Permission
            checkLocationPermission();
//                latitude = mLastLocation.getLatitude();
//                longitude = mLastLocation.getLongitude();
        }
        View maplab=(View) findViewById( R.id.mapLayout);
        startBT = findViewById(R.id.start_BT);
        customername = findViewById(R.id.customernamepaylabfull);
        testname = findViewById(R.id.testnamepayfulllab);
        walkinorhome = findViewById(R.id.walkinorhome);
        delivered=findViewById(R.id.deliveredlab);
        nomapslab=findViewById(R.id.nomapslab);
        call=findViewById(R.id.calllab);
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(LabPaymentFullDetails.this, "LabPaymentFullDetails");
//        sequence.setConfig(config);
//        sequence.addSequenceItem(delivered,"Hit,when you delivered the product successfully", "GOT IT");
//        sequence.start();
        Intent intent = getIntent();
        LabPaymentDetails labPaymentDetails = intent.getParcelableExtra("serialno");
        puid = labPaymentDetails.puid;
        destLat=Double.parseDouble(labPaymentDetails.lats);
        destLong=Double.parseDouble(labPaymentDetails.longs);
        walkinorhome1 = labPaymentDetails.walkinorhome;
        if (walkinorhome1.equals("Walkin"))
        {
            maplab.setVisibility(View.INVISIBLE);
            startBT.setVisibility(View.INVISIBLE);
            nomapslab.setVisibility(View.VISIBLE);

        }
        Geocoder geocoder = new Geocoder(LabPaymentFullDetails.this, Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(destLat,destLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city = addresses.get(0).getSubAdminArea().toLowerCase();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maplab);
        mapFragment.getMapAsync(this);
        init();
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPS) {
            //  showGPSSettingsAlert();
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            showGPSSettingsAlert();
                        }
                    })
                    .create()
                    .show();

        }
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference();
        if(labPaymentDetails.isdelivered)
            delivered.setEnabled(false);
        delivered.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                databaseReference2.child("Payment Report").child("LabTest").child(labPaymentDetails.duid).child(labPaymentDetails.payid).child("isdelivered").setValue(true);
            }
        });
        serialno = labPaymentDetails.serialno;
        String[] arr =serialno.split(",");
        testname.setText("Tests:");
        for(int i=1;i<arr.length;i++)
        {
            if(!arr[i].isEmpty()) {
                System.out.println(city);
                databaseReference1 = firebaseDatabase.getReference().child("Labtests").child(city.toLowerCase()).child(uid).child("" + arr[i]);
                System.out.println(databaseReference1);
                databaseReference = firebaseDatabase.getReference().child("Patient Database").child(puid);
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        System.out.println(dataSnapshot1.getKey());
                        String labname = dataSnapshot1.child("testname").getValue().toString();
                        testname.append(labname + ",");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PatientDetails patientDetails = dataSnapshot.getValue(PatientDetails.class);
                mobilenumber=patientDetails.phone;
                customername.setText("Name:" + patientDetails.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        walkinorhome.setText("Walk in/home:" + walkinorhome1);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intentphone = new Intent();
                intentphone.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intentphone.setData(Uri.parse("tel: " + mobilenumber)); // Data with intent respective action on intent
                startActivity(intentphone);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);
                // location is received
                mLastLocation = locationResult.getLastLocation();
                System.out.print(mLastLocation.getLatitude());
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(destLat, destLong);
        if(destLat==0.0 || destLong==0.0)
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in selected lab"));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Your Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            catch (Exception e){}
        }

        Log.i(TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+location.getLatitude());
        lats=mLastLocation.getLatitude();
        longs=mLastLocation.getLongitude();
        if (startTrack) {

            previouslatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (m != null)
                m.remove();
            m = mMap.addMarker(new MarkerOptions().draggable(true).title("I am here ").
                    position(previouslatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.walk)));
            if (origin.latitude != mLastLocation.getLatitude() && origin.longitude != mLastLocation.getLongitude()) {

            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LabPaymentFullDetails.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            }
        }
    }

    public void showGPSSettingsAlert() {
        isGPS = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.word_GPS);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(R.string.word_GPS_not_enabled);
        alertDialog.setPositiveButton(R.string.word_GPS_enable, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), PERMISSION_REQUEST_GPS_CODE);
            }
        });

        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();

                        }
                        mMap.setMyLocationEnabled(true);
                        //  addMarker(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_GPS_CODE) {
            isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPS)
                showGPSSettingsAlert();


        }
    }

    public void drawRoute(View view)
    {
        origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        dest = new LatLng(destLat, destLong);
        String url = getUrl(origin,dest);
        FetchUrl FetchUrl = new FetchUrl();
        FetchUrl.execute(url);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        startTrack = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String getUrl(LatLng origin, LatLng dest)
    {
        //  String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + MY_API_KEY
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //Toast.makeText(LabPaymentFullDetails.this,str_origin,Toast.LENGTH_LONG).show();
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Toast.makeText(LabPaymentFullDetails.this,str_dest,Toast.LENGTH_LONG).show();



        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters
                + "&key=" + "AIzaSyDhtxhgD7V85U6h72aV-QUvq54y51OTKRU";
        return url;
    }

    private class FetchUrl extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... url)
        {
            // For storing data from web service
            String data = "";
            try {
                data = downloadUrl(url[0]);
                Log.e("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
        {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DirectionsJSONParser parser = new DirectionsJSONParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            //  ArrayList<LatLng> points;
            lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}