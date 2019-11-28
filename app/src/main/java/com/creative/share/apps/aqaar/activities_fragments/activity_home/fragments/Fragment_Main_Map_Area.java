package com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.databinding.FragmentMainMapAreaBinding;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Main_Map_Area extends Fragment implements OnMapReadyCallback {
    private FragmentMainMapAreaBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private GoogleMap mMap;
    private List<LatLng> latLngs;
    private List<String> titles;




    public static Fragment_Main_Map_Area newInstance() {
        return new Fragment_Main_Map_Area();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_map_area,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        latLngs = new ArrayList<>();
        titles = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        initMap();


        latLngs.add(new LatLng(30.972150,41.013330));
        latLngs.add(new LatLng(26.236450,36.462749));
        latLngs.add(new LatLng(27.534140,41.698120));
        latLngs.add(new LatLng(22.272600,46.730450));

        titles.add("عرعر");
        titles.add("تبوك");
        titles.add("حائل");
        titles.add("الرياض");

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


            LatLngBounds.Builder bounds = new LatLngBounds.Builder();

            int i = 0;
            for (LatLng latLng :latLngs)
            {
                bounds.include(latLng);
                addMarker(latLng,titles.get(i));
                i++;

            }

            binding.progBar.setVisibility(View.GONE);

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),200));
        }
    }

    private void addMarker(LatLng latLng,String title)
    {

        IconGenerator iconGenerator = new IconGenerator(activity);
        iconGenerator.setBackground(ContextCompat.getDrawable(activity,R.drawable.marker_city_bg));
        iconGenerator.setContentPadding(25,25,0,0);
        iconGenerator.setTextAppearance(R.style.iconGenText);

        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(title))));

    }
}
