package com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_ad2_details.Ad2DetailsActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_ad_details.AdDetailsActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.databinding.FragmentMainMapMyLocationBinding;
import com.creative.share.apps.aqaar.models.AdDataModel;
import com.creative.share.apps.aqaar.models.AdModel;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.tags.Tags;
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
import com.google.android.material.tabs.TabLayout;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private List<CategoryDataModel.CategoryModel> categoryModelList;
    private double lat=0.0,lng = 0.0;



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
        categoryModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        initMap();

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos==0)
                {
                    getAds("all",String.valueOf(lat),String.valueOf(lng),"all");

                }else
                {
                    CategoryDataModel.CategoryModel categoryModel = categoryModelList.get(pos);

                    getAds("all","all","all",String.valueOf(categoryModel.getId()));

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



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
            mMap.setMyLocationEnabled(true);
            mMap.setInfoWindowAdapter(new WindowInfo());
            mMap.setOnInfoWindowClickListener(marker -> {
                AdModel adModel = (AdModel) marker.getTag();
                if (adModel!=null)
                {
                    if (adModel.getMain_cat_id_fk()==1)
                    {
                        Intent intent = new Intent(activity, Ad2DetailsActivity.class);
                        intent.putExtra("data",adModel);
                        startActivity(intent);
                    }else
                    {
                        Intent intent = new Intent(activity, AdDetailsActivity.class);
                        intent.putExtra("data",adModel);
                        startActivity(intent);
                    }
                }
            });
            getAllCategories();
            checkPermission();


        }
    }

    private void getAllCategories() {

        Api.getService(Tags.base_url)
                .getAllCategories()
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            updateTabUI(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryDataModel> call, Throwable t) {

                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });



    }

    private void getAds(String city_id,String lat,String lng,String category_id) {
        mMap.clear();
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getAdsByCityAndCategory(city_id,lat,lng,category_id)
                .enqueue(new Callback<AdDataModel>() {
                    @Override
                    public void onResponse(Call<AdDataModel> call, Response<AdDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            updateDataMapUI(response.body().getData());
                        }else
                        {
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error code",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code()==500)
                            {
                                Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);

                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });

    }


    private void updateTabUI(List<CategoryDataModel.CategoryModel> data) {
        CategoryDataModel.CategoryModel categoryModel = new CategoryDataModel.CategoryModel(0,"الكل","All");
        data.add(0,categoryModel);
        categoryModelList.clear();
        categoryModelList.addAll(data);
        for (CategoryDataModel.CategoryModel categoryModel1:data)
        {
            if (lang.equals("ar"))
            {
                binding.tab.addTab(binding.tab.newTab().setText(categoryModel1.getTitle_ar()));

            }else
            {
                binding.tab.addTab(binding.tab.newTab().setText(categoryModel1.getTite_en()));

            }
        }

        new Handler().postDelayed(
                () -> binding.tab.getTabAt(0).select(), 100);


    }

    private void updateDataMapUI(List<AdModel> data)
    {
        if (data.size()>0)
        {
            for (AdModel adModel :data)
            {
                addMarker(adModel);
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),6.3f));
        }else
        {
            Toast.makeText(activity, R.string.no_ads, Toast.LENGTH_LONG).show();
        }
    }
    private void addMarker(AdModel adModel)
    {
        IconGenerator iconGenerator = new IconGenerator(activity);
        iconGenerator.setContentPadding(15,15,15,15);
        iconGenerator.setTextAppearance(R.style.iconGenText);
        iconGenerator.setBackground(ContextCompat.getDrawable(activity,R.drawable.marker1_bg));
        Marker marker;


        if (adModel!=null&&adModel.getMain_cat_id_fk()==1)
        {

            if (adModel.getOther_metr_price()!=null&&!adModel.getOther_metr_price().isEmpty())
            {
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(adModel.getAqar_lat(),adModel.getAqar_long())).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(adModel.getOther_metr_price()+" "+getString(R.string.sar)))));

            }else
            {
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(adModel.getAqar_lat(),adModel.getAqar_long())).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(getString(R.string.no_price)))));

            }

        }else
        {

            if (adModel!=null&&adModel.getMetr_price()!=null&&!adModel.getMetr_price().isEmpty())
            {
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(adModel.getAqar_lat(),adModel.getAqar_long())).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(adModel.getMetr_price()+" "+getString(R.string.sar)))));

            }else
            {
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(adModel.getAqar_lat(),adModel.getAqar_long())).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(getString(R.string.no_price)))));

            }


        }
        marker.setTag(adModel);


    }
    public  class WindowInfo implements GoogleMap.InfoWindowAdapter
    {

        @Override
        public View getInfoWindow(Marker marker) {

            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            AdModel adModel = (AdModel) marker.getTag();
            if (adModel==null)
            {
                return null;
            }else
            {
                View view = LayoutInflater.from(activity).inflate(R.layout.window_info_view,null);
                TextView tvTitle = view.findViewById(R.id.tvTitle);
                TextView tvPrice = view.findViewById(R.id.tvPrice);
                TextView tvDetails = view.findViewById(R.id.tvDetails);
                TextView tvAddress = view.findViewById(R.id.tvAddress);
                ImageView image = view.findViewById(R.id.image);

                ProgressBar progBar = view.findViewById(R.id.progBar);

                try {

                    if (adModel.getAqar_title()!=null&&!adModel.getAqar_title().isEmpty())
                    {
                        tvTitle.setText(adModel.getAqar_title());

                    }else
                    {
                        tvTitle.setText(getString(R.string.no_name));

                    }

                    if (adModel.getMain_cat_id_fk()==1)
                    {



                        if (adModel.getOther_metr_price()!=null&&!adModel.getOther_metr_price().isEmpty())
                        {
                            tvPrice.setText(String.format("%s %s",adModel.getOther_metr_price(),getString(R.string.sar)));

                        }else
                        {
                            tvPrice.setText(getString(R.string.no_price));

                        }



                        if (adModel.getOther_aqar_text()!=null&&!adModel.getOther_aqar_text().isEmpty())
                        {
                            tvDetails.setText(adModel.getOther_aqar_text());

                        }else
                        {
                            tvDetails.setText(getString(R.string.no_details));

                        }

                        if (adModel.getAqar_makan()!=null&&!adModel.getAqar_makan().isEmpty())
                        {
                            tvAddress.setText(String.format("%s %s",adModel.getAqar_makan(),adModel.getCity_name()));

                        }else
                        {
                            tvAddress.setText(adModel.getCity_name());

                        }


                    }else
                    {
                        if (adModel.getAqar_title()!=null&&!adModel.getAqar_title().isEmpty())
                        {
                            tvTitle.setText(adModel.getAqar_title());

                        }else
                        {
                            tvTitle.setText(getString(R.string.no_name));

                        }

                        if (adModel.getMetr_price()!=null&&!adModel.getMetr_price().isEmpty())
                        {
                            tvPrice.setText(String.format("%s %s",adModel.getMetr_price(),getString(R.string.sar)));

                        }else
                        {
                            tvPrice.setText(getString(R.string.no_price));

                        }



                        if (adModel.getAqar_text()!=null&&!adModel.getAqar_text().isEmpty())
                        {
                            tvDetails.setText(adModel.getAqar_text());

                        }else
                        {
                            tvDetails.setText(getString(R.string.no_details));

                        }

                        if (adModel.getAqar_makan()!=null&&!adModel.getAqar_makan().isEmpty())
                        {
                            tvAddress.setText(String.format("%s %s",adModel.getAqar_makan(),adModel.getCity_name()));

                        }else
                        {
                            tvAddress.setText(adModel.getCity_name());

                        }
                    }



                    Picasso.with(activity).load(Uri.parse(Tags.base_url+adModel.getImage())).fit().into(image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progBar.setVisibility(View.GONE);

                        }
                    });

                }catch (Exception e)
                {
                    if (e!=null&&e.getMessage()!=null)
                    {
                        Log.e("error",e.getMessage());
                    }
                }

                return view;
            }

        }
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
        lat = location.getLatitude();
        lng = location.getLongitude();
        getAds("all",String.valueOf(lat),String.valueOf(lng),"all");
        stopUpdateLocation();
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
