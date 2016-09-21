package com.sampi.luigirusso.thebookhunt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sampi.luigirusso.thebookhunt.controllers.BookController;
import com.sampi.luigirusso.thebookhunt.entities.Book;

import java.util.ArrayList;


public class MapsActivity extends BaseNavActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 57;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private BookController bookController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.setState(savedInstanceState);
        super.onCreateDrawer();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        bookController = new BookController("testData");
        ArrayList<BookController.Category> bookCategories = bookController.getCategories();
        for(int i=0; i<bookCategories.size();i++){
            ArrayList<Book> booksToDisplay = bookCategories.get(i).getBooks();
            for(int j=0; j<booksToDisplay.size();j++){
                LatLng pos = new LatLng(booksToDisplay.get(j).getLatitude(),booksToDisplay.get(j).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .snippet(booksToDisplay.get(j).getAuthor())
                        .title(booksToDisplay.get(j).getTitle()));
            }
        }

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(50, 10);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            try {
                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                LatLng current = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(current).title("You're here!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
            }catch (NullPointerException e) {
                Toast.makeText(this, "Location not enabled", Toast.LENGTH_SHORT).show();
            }
                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPosition);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateLocationAndCenter();
                    }
                });

        }
    }

    public void updateLocationAndCenter() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                LatLng current = new LatLng(lat, lon);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
            }catch (NullPointerException e){
                /*TODO: make this work*/
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Location disabled")
                        .setMessage("You need to enable location services to check your position")
                        .show();
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}