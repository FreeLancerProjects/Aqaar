package com.creative.share.apps.aqaar.activities_fragments.activity_add2;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aqaar.adapters.CityAdapter;
import com.creative.share.apps.aqaar.adapters.ImagesAdapter;
import com.creative.share.apps.aqaar.adapters.Spinner_Type_Adapter;
import com.creative.share.apps.aqaar.databinding.ActivityAdd1Binding;
import com.creative.share.apps.aqaar.databinding.ActivityBuildAndContractBinding;
import com.creative.share.apps.aqaar.databinding.DialogSelectImageBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
import com.creative.share.apps.aqaar.models.CityDataModel;
import com.creative.share.apps.aqaar.models.Order2_Upload_Model;
import com.creative.share.apps.aqaar.models.Order_Upload_Model;
import com.creative.share.apps.aqaar.models.SelectedLocation;
import com.creative.share.apps.aqaar.models.TypeDataModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.share.Common;
import com.creative.share.apps.aqaar.tags.Tags;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add1Activity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAdd1Binding binding;

    private String lang;



    private List<TypeDataModel.TypeModel> typeModelList;
    private List<CityDataModel.CityModel> cDataList;

    private CityAdapter cityadapter;
    private Spinner_Type_Adapter typeAdapter;
    private Order2_Upload_Model order_upload_model;
    private Preferences preferences;
    private UserModel userModel;
    private String city_id, type_id, cat_id;
    private CategoryDataModel.CategoryModel categoryModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add1);
        initView();

        //getDepartments();
        //  gettypes();
        updatetypeAdapter();
        getdatafromintent();
        getCities();
    }

    private void getdatafromintent() {
        if (getIntent().getSerializableExtra("depart") != null) {
            categoryModel = (CategoryDataModel.CategoryModel) getIntent().getSerializableExtra("depart");
            cat_id = categoryModel.getId() + "";
            binding.setCatmodel(categoryModel);

        }
    }
/*
    public void gettypes() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .gettype()
                .enqueue(new Callback<TypeDataModel>() {
                    @Override
                    public void onResponse(Call<TypeDataModel> call, Response<TypeDataModel> response) {
                        //   progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                //   ll_no_order.setVisibility(View.GONE);
                                updatetypeAdapter(response.body());                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                //  ll_no_order.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(AddAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TypeDataModel> call, Throwable t) {
                        try {


                            Toast.makeText(AddAdsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }*/

    private void updatetypeAdapter() {
        if (lang.equals("ar")) {
            typeModelList.add(new TypeDataModel.TypeModel("اختر تجارى ام سكنى "));

            typeModelList.add(new TypeDataModel.TypeModel("تجارى"));
            typeModelList.add(new TypeDataModel.TypeModel("سكنى"));

        } else {

            typeModelList.add(new TypeDataModel.TypeModel("choose Commercial Or residential"));
            typeModelList.add(new TypeDataModel.TypeModel("commercial"));
            typeModelList.add(new TypeDataModel.TypeModel("residential"));


        }
        typeAdapter.notifyDataSetChanged();


    }

    private void initView() {
        order_upload_model = new Order2_Upload_Model();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        cDataList = new ArrayList<>();

        typeModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

        typeAdapter = new Spinner_Type_Adapter(typeModelList, this);

        binding.spinnertype.setAdapter(typeAdapter);

        cityadapter = new CityAdapter(cDataList, this);
binding.spinneradcity.setAdapter(cityadapter);
        binding.spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    type_id = "";

                } else {
                    type_id = String.valueOf(i+"");


                }
                //  Log.e("cc",city_id);
                order_upload_model.setType_aqar(type_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinneradcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";

                } else {
                    city_id = String.valueOf(cDataList.get(i).getId_city());


                }
                //  Log.e("cc",city_id);
                order_upload_model.setCity_id(city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnSend.setOnClickListener(view -> {
            if (order_upload_model.isDataValidStep1(this)) {
                Intent intent = new Intent(Add1Activity.this, Add2Activity.class);
                intent.putExtra("data",order_upload_model);
                intent.putExtra("cat",cat_id);

                startActivity(intent);
                finish();

            } else {

            }
        });

    }

    private void updateCityAdapter(CityDataModel body) {

        cDataList.add(new CityDataModel.CityModel("اختر المدينه", "choose city"));
        if (body.getData() != null) {
            cDataList.addAll(body.getData());
            cityadapter.notifyDataSetChanged();
        }
    }

    private void getCities() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getAllCities()
                    .enqueue(new Callback<CityDataModel>() {
                        @Override
                        public void onResponse(Call<CityDataModel> call, Response<CityDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getData() != null) {
                                    updateCityAdapter(response.body());
                                } else {
                                    Log.e("error", response.code() + "_" + response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(Add1Activity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(Add1Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CityDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(Add1Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Add1Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }


    @Override
    public void back() {
        finish();
    }


}
