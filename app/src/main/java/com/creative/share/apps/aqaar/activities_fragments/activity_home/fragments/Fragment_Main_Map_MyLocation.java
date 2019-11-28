package com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.databinding.FragmentMainMapMyLocationBinding;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Main_Map_MyLocation extends Fragment implements OnMapReadyCallback ,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, LocationListener {
    private FragmentMainMapMyLocationBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private GoogleMap mMap;
    private Marker marker;
    private final String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int req_loc = 100,gps_req =101;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;



    public static Fragment_Main_Map_MyLocation newInstance() {
        return new Fragment_Main_Map_MyLocation();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_map_my_location,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        initMap();


    }

    private void initMap() {
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (fragment!=null)
        {
            fragment.getMapAsync(this);

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap!=null)
        {
            mMap = googleMap;
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity,R.raw.maps));
            mMap.setTrafficEnabled(false);
            checkPermission();


        }
    }


    private void getNearByAds(Location myLocation) {

    }

    private void addMyMarker(LatLng latLng)
    {
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f));

    }

    private void addMarker(LatLng latLng,String title)
    {
        IconGenerator iconGenerator = new IconGenerator(activity);
        iconGenerator.setContentPadding(5,3,5,3);
        iconGenerator.setBackground(ContextCompat.getDrawable(activity,R.drawable.marker1_bg));
        marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(title))));

    }


    private void initGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(activity,fineLocation)== PackageManager.PERMISSION_GRANTED)
        {
            initGoogleApiClient();

        }else
        {
            ActivityCompat.requestPermissions(activity,new String[]{fineLocation},req_loc);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == req_loc&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            initGoogleApiClient();
        }else
        {
            Toast.makeText(activity, "Access location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==gps_req&&resultCode== Activity.RESULT_OK)
        {
            startLocationUpdate();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(6000);
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .setAlwaysShow(false)
                .addLocationRequest(locationRequest)
                .build();
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request);
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.SUCCESS)
            {
                startLocationUpdate();
            }else if (status.getStatusCode()==LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
            {
                try {
                    status.startResolutionForResult(activity,gps_req);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        stopUpdateLocation();
        addMyMarker(new LatLng(location.getLatitude(),location.getLongitude()));
        getNearByAds(location);
    }




    private double getDistance(double lat1,double lng1,double lat2,double lng2)
    {
        return SphericalUtil.computeDistanceBetween(new LatLng(lat1,lng1),new LatLng(lat2,lng2));
    }

    private  void  stopUpdateLocation()
    {
        if (googleApiClient!=null)
        {
            googleApiClient.disconnect();
        }

        if (locationCallback!=null)
        {
            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopUpdateLocation();
    }
}
