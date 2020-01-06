package com.creative.share.apps.aqaar.activities_fragments.activity_add_ads;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
import com.creative.share.apps.aqaar.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.aqaar.adapters.CityAdapter;
import com.creative.share.apps.aqaar.adapters.ImagesAdapter;

import com.creative.share.apps.aqaar.adapters.Spinner_Type_Adapter;
import com.creative.share.apps.aqaar.databinding.ActivityBuildAndContractBinding;
import com.creative.share.apps.aqaar.databinding.DialogSelectImageBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
import com.creative.share.apps.aqaar.models.CityDataModel;
import com.creative.share.apps.aqaar.models.Order_Upload_Model;
import com.creative.share.apps.aqaar.models.SelectedLocation;
import com.creative.share.apps.aqaar.models.TypeDataModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.share.Common;
import com.creative.share.apps.aqaar.tags.Tags;
import com.google.gson.TypeAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityBuildAndContractBinding binding;
    private SelectedLocation selectedLocation;

    private String lang;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url = null;
    private List<Uri> urlList;
    private LinearLayoutManager manager, manager2;
    private ImagesAdapter imagesAdapter;
    private List<CityDataModel.CityModel> cDataList;

    private CityAdapter cityadapter;
    private List<TypeDataModel.TypeModel> typeModelList;

    private Spinner_Type_Adapter typeAdapter;
    private Order_Upload_Model order_upload_model;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_build_and_contract);
        initView();
        getCities();

        //getDepartments();
        //  gettypes();
        updatetypeAdapter();
        getdatafromintent();
    }

    private void getdatafromintent() {
        if (getIntent().getSerializableExtra("depart") != null) {
            categoryModel = (CategoryDataModel.CategoryModel) getIntent().getSerializableExtra("depart");
            cat_id = categoryModel.getId() + "";

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
            typeModelList.add(new TypeDataModel.TypeModel("اختر نوع الارض"));

            typeModelList.add(new TypeDataModel.TypeModel("تجارى"));
            typeModelList.add(new TypeDataModel.TypeModel("سكنى"));

        } else {

            typeModelList.add(new TypeDataModel.TypeModel("choose land type"));
            typeModelList.add(new TypeDataModel.TypeModel("commercial"));
            typeModelList.add(new TypeDataModel.TypeModel("residential"));


        }


    }

    private void initView() {
        order_upload_model = new Order_Upload_Model();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        urlList = new ArrayList<>();
        cDataList = new ArrayList<>();
        typeModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setOrderModel(order_upload_model);
        binding.imageSelectPhoto.setOnClickListener(view -> CreateImageAlertDialog());

        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        binding.recView.setLayoutManager(manager);
        imagesAdapter = new ImagesAdapter(urlList, this);
        binding.recView.setAdapter(imagesAdapter);
        cityadapter = new CityAdapter(cDataList, this);
        typeAdapter = new Spinner_Type_Adapter(typeModelList, this);
        binding.spinneradcity.setAdapter(cityadapter);
        binding.spinnertype.setAdapter(typeAdapter);


        binding.spinneradcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";

                } else {
                    city_id = String.valueOf(cDataList.get(i).getId_city());


                }
                Log.e("cc", city_id);
                order_upload_model.setCity_id(city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    type_id = "";

                } else {
                    type_id = String.valueOf(typeModelList.get(i).getId());


                }
                //  Log.e("cc",city_id);
                order_upload_model.setType_id(type_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAdsActivity.this, MapActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnSend.setOnClickListener(view -> {
            if (order_upload_model.isDataValidStep1(this)) {
                if (userModel != null) {
                    if (urlList != null && urlList.size() > 0) {
                        sendorderWithImage(order_upload_model);
                    } else {
                        Toast.makeText(this,getResources().getString(R.string.add_photo),Toast.LENGTH_LONG).show();
                    }
                } else {
                    //   Common.CreateNoSignAlertDialog(this);
                }

            } else {
                if (urlList == null || urlList.size() == 0) {
                    Toast.makeText(this, getResources().getString(R.string.upload_picture), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(AddAdsActivity.this, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }


    private void sendorderWithImage(Order_Upload_Model order_upload_model) {
        final Dialog dialog = Common.createProgressDialog(AddAdsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        //  Log.e("data",userModel.getUser().getId()+" "+order_upload_model.getCategory_id()+" "+order_upload_model.getSubcategory_id()+" "+order_upload_model.getCity_id()+" "+type_id+" "+order_upload_model.getTitle()+" "+order_upload_model.getDetails()+" "+order_upload_model.getAddress()+" "+order_upload_model.getLongitude()+" "+order_upload_model.getLatitude()+" "+views_num+" "+is_Special+" "+is_Install+" "+commented);
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");

        RequestBody category_part = Common.getRequestBodyText(cat_id);
        RequestBody type_part = Common.getRequestBodyText(type_id);
        RequestBody city_part = Common.getRequestBodyText(order_upload_model.getCity_id());
        RequestBody title_part = Common.getRequestBodyText(order_upload_model.getTitle());

        RequestBody interface_part = Common.getRequestBodyText(order_upload_model.getOther_interface());
        RequestBody detials_part = Common.getRequestBodyText(order_upload_model.getDetails());
        RequestBody price_part;
            price_part = Common.getRequestBodyText(order_upload_model.getPrice());


        RequestBody address_part;
        RequestBody long_part;
        RequestBody lat_part;
        RequestBody other_size_part;
        RequestBody other_street_width_part;
            address_part = Common.getRequestBodyText(order_upload_model.getAddress());
            long_part = Common.getRequestBodyText(order_upload_model.getLongitude());
            lat_part = Common.getRequestBodyText(order_upload_model.getLatitude());

            other_size_part = Common.getRequestBodyText(order_upload_model.getOther_size());

        // RequestBody is_Special_part=Common.getRequestBodyText(is_Special+"");
        //RequestBody is_Install_part=Common.getRequestBodyText(is_Install+"");
        //RequestBody commented_part=Common.getRequestBodyText(commented+"");
            other_street_width_part = Common.getRequestBodyText(order_upload_model.getOther_street_width());



        List<MultipartBody.Part> partimageList = getMultipartBodyList(urlList, "image[]");
        try {
            Api.getService(Tags.base_url)
                    .Sendorder(user_part, category_part, city_part, type_part, title_part, detials_part, price_part, address_part, long_part, lat_part, interface_part, other_street_width_part,other_size_part, partimageList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(AddAdsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        //  Intent intent = new Intent(AddAdsActivity.this, AddAdsActivity.class);

                        //startActivity(intent);
                        finish();
                    } else {

                        try {

                            Toast.makeText(AddAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody().string() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(AddAdsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }

    private void updateCityAdapter(CityDataModel body) {

        cDataList.add(new CityDataModel.CityModel("اختر", "choose"));
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
                                    Toast.makeText(AddAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(AddAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(AddAdsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }


    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            url = getUriFromBitmap(bitmap);
            urlList.add(url);
            imagesAdapter.notifyDataSetChanged();


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            url = data.getData();
            urlList.add(url);
            imagesAdapter.notifyDataSetChanged();


        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("location")) {
                selectedLocation = (SelectedLocation) data.getSerializableExtra("location");
                binding.setLocation(selectedLocation);
                order_upload_model.setLatitude(String.valueOf(selectedLocation.getLat()));
                order_upload_model.setLongitude(String.valueOf(selectedLocation.getLng()));
                order_upload_model.setAddress(selectedLocation.getAddress());
                binding.setOrderModel(order_upload_model);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    public void deleteImage(int adapterPosition) {
        urlList.remove(adapterPosition);
        imagesAdapter.notifyItemRemoved(adapterPosition);

    }

    @Override
    public void back() {
        finish();
    }


}
