package com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_my_profile.MyProfileActivity;
import com.creative.share.apps.aqaar.adapters.MyAdsAdapter;
import com.creative.share.apps.aqaar.databinding.FragmentProfileBinding;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.share.Common;
import com.creative.share.apps.aqaar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment{
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private String lang;

    private UserModel userModel;
    private Preferences preferences;

    private List<UserModel.AdModel> adModels;
    private MyAdsAdapter myAdsAdapter;



    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        preferences = Preferences.newInstance();
activity=(HomeActivity)getActivity(); 
        userModel = preferences.getUserData(activity);


//    Log.e("y",userModel.getUser().getId()+"");

        adModels=new ArrayList<>();
        myAdsAdapter=new MyAdsAdapter(adModels,activity);



        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.setUsermodel(userModel.getUser());

        binding.recMyads.setLayoutManager(new GridLayoutManager(activity,1));
        binding.recMyads.setAdapter(myAdsAdapter);
        if(userModel!=null){
            getprofiledata();}


    }
    private void getprofiledata() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getmyprofile(userModel.getUser().getId()+"")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateprofile(response.body());
                            } else {

                             //   Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error_data5", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                      Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                     //   Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            if(dialog!=null){
                dialog.dismiss();}

            // Log.e("err", e.getMessage());
        }
    }

    private void updateprofile(UserModel userModel) {
        binding.setUsermodel(userModel.getUser());
        this.userModel=userModel;
        if(userModel.getAds()!=null) {
            adModels.clear();

            adModels.addAll(userModel.getAds());
            myAdsAdapter.notifyDataSetChanged();
        }
    }


}
