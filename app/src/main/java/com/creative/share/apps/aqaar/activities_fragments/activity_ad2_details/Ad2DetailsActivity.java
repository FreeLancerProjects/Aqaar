package com.creative.share.apps.aqaar.activities_fragments.activity_ad2_details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_ad_details.FragmentMapTouchListener;
import com.creative.share.apps.aqaar.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aqaar.activities_fragments.other_profile.OtherProfileActivity;
import com.creative.share.apps.aqaar.databinding.ActivityAd2DetailsBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.AdModel;
import com.creative.share.apps.aqaar.tags.Tags;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;

public class Ad2DetailsActivity extends AppCompatActivity implements Listeners.BackListener, OnMapReadyCallback {
    private ActivityAd2DetailsBinding binding;
    private String lang;
    private AdModel adModel;
    private final float zoom = 11.0f;
    private FragmentMapTouchListener fragment;
    private GoogleMap mMap;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ad2_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            adModel = (AdModel) intent.getSerializableExtra("data");
        }
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setAdModel(adModel);
        Log.e("data",Tags.base_url+adModel.getImage());
        Picasso.with(this).load(Uri.parse(Tags.base_url+adModel.getImage())).fit().into(binding.image);
        initMap();
        binding.tvuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adModel.getUser_name()!=null){
                Intent intent = new Intent(Ad2DetailsActivity.this, OtherProfileActivity.class);
                intent.putExtra("id",adModel.getUser_id()+"");
                startActivity(intent);
                finish();}
            }
        });

    }

    private void initMap() {

        fragment = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);
        if (fragment != null) {
            fragment.getMapAsync(this);

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            fragment.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));
            AddMarker(adModel.getAqar_lat(), adModel.getAqar_long(), adModel.getAqar_title());
        }
    }

    private void AddMarker(double lat, double lng, String title) {

        if (title != null && !title.isEmpty()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).setTitle(title);

        } else {
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


    }

    @Override
    public void back() {
        finish();
    }


}
