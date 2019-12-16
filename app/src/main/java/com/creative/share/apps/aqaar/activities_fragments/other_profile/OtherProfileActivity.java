package com.creative.share.apps.aqaar.activities_fragments.other_profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.aqaar.adapters.MyAdsAdapter;
import com.creative.share.apps.aqaar.databinding.ActivityMyProfileBinding;
import com.creative.share.apps.aqaar.databinding.ActivityOtherProfileBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.AdModel;
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

public class OtherProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOtherProfileBinding binding;
    private String lang;

    private UserModel userModel;
    private Preferences preferences;
private String other_id;
private List<UserModel.AdModel> adModels;
private MyAdsAdapter myAdsAdapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_profile);
getdatafromintent();
        initView();


    }

    private void getdatafromintent() {
        other_id=getIntent().getStringExtra("id");
    }


    private void initView() {
        preferences = Preferences.newInstance();



//    Log.e("y",userModel.getUser().getId()+"");
userModel=new UserModel();
adModels=new ArrayList<>();
myAdsAdapter=new MyAdsAdapter(adModels,this);



        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

        binding.setUsermodel(userModel.getUser());

binding.recMyads.setLayoutManager(new GridLayoutManager(this,1));
binding.recMyads.setAdapter(myAdsAdapter);
binding.btnemail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(preferences.getUserData(OtherProfileActivity.this)!=null){
        if(userModel.getUser()!=null){
        Intent intent=new Intent(OtherProfileActivity.this, ChatActivity.class);
        intent.putExtra("data",userModel.getUser().getId()+"");
        intent.putExtra("name",userModel.getUser().getUser_name());
        intent.putExtra("phone",userModel.getUser().getUser_phone());

        startActivity(intent);}}

        else {
            Common.CreateDialogAlert(OtherProfileActivity.this, getString(R.string.please_sign_in_or_sign_up));

        }
    }
});
        if(userModel!=null){
        getprofiledata();}


    }





    @Override
    public void back() {
        finish();
    }

    private void getprofiledata() {
       final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getmyprofile(other_id)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateprofile(response.body());
                            } else {

                                Toast.makeText(OtherProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                        Toast.makeText(OtherProfileActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OtherProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        this.userModel=userModel;
        binding.setUsermodel(userModel.getUser());

        if(userModel.getAds()!=null) {
            adModels.clear();

            adModels.addAll(userModel.getAds());
            myAdsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1002){
            //userModel=preferences.getUserData(this);
            //updateprofile(userModel);
          //  getprofiledata();
        }
    }


}
