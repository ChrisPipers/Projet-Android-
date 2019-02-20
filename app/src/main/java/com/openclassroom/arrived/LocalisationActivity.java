package com.openclassroom.arrived;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import android.location.Address;
import android.widget.Toast;

import model.User;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class LocalisationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "LocalisationActivity";

    private TextView mPresRue = null;
    private TextView mRue = null;
    private TextView mPresNumero = null;
    private TextView mNumero = null;
    private TextView mPresBoite = null;
    private TextView mBoite = null;
    private TextView mPresCode = null;
    private TextView mCode = null;
    private TextView mPresLocalite = null;
    private TextView mLocalite = null;
    private Button mButValide = null;
    //private Button mButReload;

    private GoogleMap mMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1212;
    private static final float DEFAULT_ZOOM = 10.0f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    //localisation
    private Boolean mLocationPermissionsGranted = true;
    private List<Address> addresses;
    private Geocoder geocoder;
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private Location localisation;

    private FirebaseAuth mAuth;
    private FirebaseUser mFUser;
    private FirebaseDatabase mFDatabase;
    private DatabaseReference mFRefDatabase;

    ArrayList<User> userArrayList;
    ArrayAdapter<User> adaptater;
    ArrayList<String> allAdresse = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);
        init();
        clicButValPos();
        getLocationPermission();
        verifPermission();
        onLocationChanged(localisation);
        recoveryAdresse();
    }

    private void init() {
        mPresRue = (TextView) findViewById(R.id.textv_pres_rue);
        mRue = (TextView) findViewById(R.id.textv_rue);

        mPresNumero = (TextView) findViewById(R.id.textv_pres_numero);
        mNumero = (TextView) findViewById(R.id.textv_numero);

        mPresCode = (TextView) findViewById(R.id.textv_pres_codepostal);
        mCode = (TextView) findViewById(R.id.textv_codepostal);

        mPresLocalite = (TextView) findViewById(R.id.textv_pres_localite);
        mLocalite = (TextView) findViewById(R.id.textv_localite);

        mButValide = (Button) findViewById(R.id.but_validate_pos);

        geocoder = new Geocoder(this, Locale.getDefault());

    }


    //***** GERRER les permissions
    // 2
    // demande autorisation à l'utilisateur, getLocationPermission verif si permission ok
    private void verifPermission() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        localisation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
    }

    // 3
    // gerer le resultat de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialise  map
                    onCreatMap();
                }
            }
        }
    }

    // 1 ecrirer le fragment
    // 2 ajouter dans onCreate

    /**
     * 3
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.activity_localisation, (LinearLayout) findViewById(R.id.map), false);
                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());
                return infoWindow;
            }
        });
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                onCreatMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(LocalisationActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    // 4 creer le support map fragment
    public void onCreatMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(LocalisationActivity.this);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    void recoveryAdresse() {
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            mRue.setText(addresses.get(0).getThoroughfare());
            mNumero.setText(addresses.get(0).getFeatureName());
            mCode.setText(addresses.get(0).getPostalCode());
            mLocalite.setText(addresses.get(0).getLocality());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    private void clicButtonReload()
    {

    }
*/

    private void clicButValPos() {
        //TODO ajouter le faite d envoyer les données sur la data base

        this.mButValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataOnFirebase();
                Intent intent = new Intent(LocalisationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    private void updateDataOnFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mFUser = mAuth.getCurrentUser();
        final String UID = mFUser.getUid();
        mFDatabase = FirebaseDatabase.getInstance();
        Intent it = getIntent();
        updateAdresse();
        updateCpt();
        //updateMode(it);
        //updateMeteo(it);
    }

    private void updateAdresse() {
        mFRefDatabase = mFDatabase.getReference("utilisateurs").child(mFUser.getUid()).child("mAdresse");
        mFRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> listTemp = (ArrayList<String>) dataSnapshot.getValue();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs").child(mFUser.getUid()).child("mAdresse");

                databaseReference.setValue(listTemp);
                if (listTemp != null) {
                    if (!listTemp.contains(addresses.get(0).getAddressLine(0))) {
                        listTemp.add(addresses.get(0).getAddressLine(0));
                    }
                } else {
                    listTemp = new ArrayList<>();
                    listTemp.add(addresses.get(0).getAddressLine(0));
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs").child(mFUser.getUid()).child("mAdresse");
                databaseReference.setValue(listTemp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateCpt() {
        mFRefDatabase = mFDatabase.getReference("utilisateurs").child(mFUser.getUid()).child("mAllActivity");
        mFRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFUser = mAuth.getCurrentUser();
                int temp = dataSnapshot.getValue(Integer.class).intValue();
                //temp +=1;
                mFRefDatabase.setValue(0);
                mFRefDatabase.setValue(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void updateMode(Intent it) {
        final ArrayList<String> listItemMode = (ArrayList<String>) it.getSerializableExtra("ITEMS_MODE");

        mFRefDatabase = mFDatabase.getReference("utilisateurs").child(mFUser.getUid()).child("mMapMode");
        mFRefDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> mapMode = (Map<String, String>) dataSnapshot.getValue();
                if (mapMode != null) {
                    for (int i = 0; i < listItemMode.size(); i++) {
                        Log.i(TAG, "test voit valor opacity 11111111111111111111111111111111111" + mapMode.size());
                        Log.i(TAG, "test voit valor opacity 222222222222222222222222");

                        Iterator myVeryOwnIterator = mapMode.keySet().iterator();
                        while (myVeryOwnIterator.hasNext()) {
                            if (((String) myVeryOwnIterator.next()).equals(listItemMode.get(i))) {
                                String key = listItemMode.get(i);
                                String value = (mapMode.get(key)).toString();
                                int temp = Integer.parseInt(value);
                                temp += 1;
                                value = "" + temp;
                                mapMode.put(listItemMode.get(i), value);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < listItemMode.size(); i++) {
                        mapMode.replace(listItemMode.get(i), mapMode.get(listItemMode.get(i)) + 1);
                    }
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs").child(mFUser.getUid()).child("mMapMode");
                databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs").child(mFUser.getUid()).child("mMapMode");
                databaseReference.setValue(mapMode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void updateMeteo(Intent it) {
        final ArrayList<String> listItemMeteo = (ArrayList<String>) it.getSerializableExtra("ITEMS_METEO");
        mFRefDatabase = mFDatabase.getReference("utilisateurs").child(mFUser.getUid()).child("mMapMeteo");
        mFRefDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Integer> mapMeteo = (HashMap<String, Integer>) dataSnapshot.getValue();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs").child(mFUser.getUid()).child("mMapMeteo");
                databaseReference.setValue(listItemMeteo);
                if (mapMeteo != null) {
                    for (int i = 0; i < listItemMeteo.size(); i++) {
                        mapMeteo.replace(listItemMeteo.get(i), mapMeteo.get(listItemMeteo.get(i)) + 1);
                    }
                } else {
                    mapMeteo = new HashMap<String, Integer>();
                    for (int i = 0; i < listItemMeteo.size(); i++) {
                        mapMeteo.replace(listItemMeteo.get(i), mapMeteo.get(listItemMeteo.get(i)) + 1);
                    }
                }
                databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs").child(mFUser.getUid()).child("mMapMeteo");
                databaseReference.setValue(mapMeteo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onBackPressed() {
        return;
    }


}
