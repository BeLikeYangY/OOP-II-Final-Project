package com.example.android.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double latitude;
    double longitude;
    PlaceInfo placeVisited;
    String placeSearched;
    ArrayList<PlaceInfo> placeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getCurrentLocation();

        LatLng latLng = new LatLng(longitude, latitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("I am here!"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void onSearch(View view) throws IOException {
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        placeSearched = location_tf.getText().toString();
        List<Address> addressList = null;
        if (placeSearched != null || placeSearched.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(placeSearched, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLatitude());

            mMap.addMarker(new MarkerOptions().position(latLng).title(placeSearched));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    }

    public void getCurrentLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        longitude = location.getLongitude();
        latitude = location.getLatitude();

        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        location_tf.setText("");
        placeSearched = "";
    }

    public void onAddCity(View view) {
        placeVisited = new PlaceInfo(longitude, latitude, placeSearched);
        placeList.add(placeVisited);
        Toast toast = Toast.makeText(this, placeSearched, Toast.LENGTH_LONG);
        toast.show();
    }

}