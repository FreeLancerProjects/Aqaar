package com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_ad_details.AdDetailsActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.adapters.AdsAdapter;
import com.creative.share.apps.aqaar.databinding.FragmentMainAdBinding;
import com.creative.share.apps.aqaar.models.AdDataModel;
import com.creative.share.apps.aqaar.models.AdModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main_Ads extends Fragment{
    private static final String TAG1 = "city_id";
    private static final String TAG2 = "lat";
    private static final String TAG3 = "lng";
    private static final String TAG4 = "category_id";

    private FragmentMainAdBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<AdModel> adModelList;
    private String city_id,lat,lng,category_id;
    private AdsAdapter adapter;
    public static Fragment_Main_Ads newInstance(String city_id,String lat,String lng,String category_id) {

        Bundle bundle = new Bundle();
        bundle.putString(TAG1,city_id);
        bundle.putString(TAG2,lat);
        bundle.putString(TAG3,lng);
        bundle.putString(TAG4,category_id);
        Fragment_Main_Ads fragment_main_ads = new Fragment_Main_Ads();
        fragment_main_ads.setArguments(bundle);
        return fragment_main_ads;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_ad,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView()
    {
        adModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            city_id = bundle.getString(TAG1);
            lat = bundle.getString(TAG2);
            lng = bundle.getString(TAG3);
            category_id = bundle.getString(TAG4);


        }

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new AdsAdapter(adModelList,activity,this);
        binding.recView.setAdapter(adapter);
        getAds(city_id,lat,lng,category_id);

    }

    private void getAds(String city_id,String lat,String lng,String category_id)
    {
        Api.getService(Tags.base_url)
                .getAdsByCityAndCategory(city_id,lat,lng,category_id)
                .enqueue(new Callback<AdDataModel>() {
                    @Override
                    public void onResponse(Call<AdDataModel> call, Response<AdDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            adModelList.clear();
                            adModelList.addAll(response.body().getData());

                            if (adModelList.size()>0)
                            {
                                binding.tvNoAds.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }else
                                {
                                    binding.tvNoAds.setVisibility(View.VISIBLE);
                                }

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


    public void setItemData(AdModel adModel) {
        if (adModel!=null)
        {
            Intent intent = new Intent(activity, AdDetailsActivity.class);
            intent.putExtra("data",adModel);
            startActivity(intent);
        }
    }
}
