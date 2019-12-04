package com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.databinding.FragmentMainMapAreaBinding;
import com.creative.share.apps.aqaar.models.CityDataModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.tags.Tags;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main_Map_Area extends Fragment implements OnMapReadyCallback {
    private FragmentMainMapAreaBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private GoogleMap mMap;
    private List<CityDataModel.CityModel> cityModelList;


    public static Fragment_Main_Map_Area newInstance() {
        return new Fragment_Main_Map_Area();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_map_area, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        cityModelList = new ArrayList<>();

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
        if (fragment != null) {
            fragment.getMapAsync(this);

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {

            mMap = googleMap;
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.885942,45.079163),3.3f));


            getAllCities();


            mMap.setOnMarkerClickListener(marker -> {

                CityDataModel.CityModel cityModel = (CityDataModel.CityModel) marker.getTag();
                if (cityModel != null) {
                    activity.DisplayFragmentMainMap(cityModel);

                }
                return false;
            });
        }
    }

    private void getAllCities() {
        Api.getService(Tags.base_url)
                .getAllCities()
                .enqueue(new Callback<CityDataModel>() {
                    @Override
                    public void onResponse(Call<CityDataModel> call, Response<CityDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            updateMapData(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<CityDataModel> call, Throwable t) {
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

    private void updateMapData(List<CityDataModel.CityModel> data) {

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (CityDataModel.CityModel cityModel:data)
        {
            bounds.include(new LatLng(cityModel.getGoogle_latitude(),cityModel.getGoogle_longitude()));
            addMarker(cityModel);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),80));


    }

    private void addMarker(CityDataModel.CityModel cityModel) {

        IconGenerator iconGenerator = new IconGenerator(activity);
        iconGenerator.setBackground(ContextCompat.getDrawable(activity, R.drawable.marker1_bg));
        iconGenerator.setContentPadding(15, 15, 15, 15);
        iconGenerator.setTextAppearance(R.style.iconGenText);

        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(cityModel.getGoogle_latitude(),cityModel.getGoogle_longitude())).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(lang.equals("ar")?cityModel.getAr_city_title():cityModel.getEn_city_title()))));
        marker.setTag(cityModel);
    }
}
