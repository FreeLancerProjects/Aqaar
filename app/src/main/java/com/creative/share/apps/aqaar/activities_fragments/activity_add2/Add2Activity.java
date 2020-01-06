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
import com.creative.share.apps.aqaar.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.aqaar.adapters.CityAdapter;
import com.creative.share.apps.aqaar.adapters.ImagesAdapter;
import com.creative.share.apps.aqaar.adapters.Spinner_Type_Adapter;
import com.creative.share.apps.aqaar.databinding.ActivityAd2DetailsBinding;
import com.creative.share.apps.aqaar.databinding.ActivityAdd2Binding;
import com.creative.share.apps.aqaar.databinding.ActivityBuildAndContractBinding;
import com.creative.share.apps.aqaar.databinding.DialogSelectImageBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
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

import org.checkerframework.checker.units.qual.A;

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

public class Add2Activity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAdd2Binding binding;
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

    private List<TypeDataModel.TypeModel> numbathModelList;

    private Spinner_Type_Adapter numbathAdapter;
    private List<TypeDataModel.TypeModel> advertiseModelList;

    private Spinner_Type_Adapter advertiseAdapter;
    private List<TypeDataModel.TypeModel> complementModelList;

    private Spinner_Type_Adapter complementAdapter;
    private List<TypeDataModel.TypeModel> typeModelList;

    private Spinner_Type_Adapter typeAdapter;
    private List<TypeDataModel.TypeModel> garageModelList;

    private Spinner_Type_Adapter garageAdapter;
    private List<TypeDataModel.TypeModel> beriodModelList;

    private Spinner_Type_Adapter beriodAdapter;
    private List<TypeDataModel.TypeModel> kithchenModelList;

    private Spinner_Type_Adapter kitchenAdapter;
    private List<TypeDataModel.TypeModel> salonnModelList;

    private Spinner_Type_Adapter salonAdapter;
    private List<TypeDataModel.TypeModel> numroomModelList;

    private Spinner_Type_Adapter numroomAdapter;
    private List<TypeDataModel.TypeModel> leftModelList;

    private Spinner_Type_Adapter leftAdapter;
    private Order2_Upload_Model order_upload_model;
    private Preferences preferences;
    private UserModel userModel;
    private String city_id, type_id, cat_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add2);
        initView();

        //getDepartments();
        //  gettypes();
        updatetypeAdapter();
        getdatafromintent();
    }

    private void getdatafromintent() {
        if (getIntent().getStringExtra("cat") != null) {
            cat_id = getIntent().getStringExtra("cat");
            order_upload_model = (Order2_Upload_Model) getIntent().getSerializableExtra("data");
            binding.setOrderModel(order_upload_model);

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
            numbathModelList.add(new TypeDataModel.TypeModel("اختر عدد دورات المياه"));

            numbathModelList.add(new TypeDataModel.TypeModel("1"));
            numbathModelList.add(new TypeDataModel.TypeModel("2"));
            numbathModelList.add(new TypeDataModel.TypeModel("3"));

            advertiseModelList.add(new TypeDataModel.TypeModel("اختر بيع ام شراء"));
            advertiseModelList.add(new TypeDataModel.TypeModel("بيع"));
            advertiseModelList.add(new TypeDataModel.TypeModel("شراء"));
            advertiseModelList.add(new TypeDataModel.TypeModel("تبديل"));

            complementModelList.add(new TypeDataModel.TypeModel("اختر الملحق"));
            complementModelList.add(new TypeDataModel.TypeModel("يوجد"));
            complementModelList.add(new TypeDataModel.TypeModel("لا يوجد"));

            typeModelList.add(new TypeDataModel.TypeModel("اختر نوع العوائل"));
            typeModelList.add(new TypeDataModel.TypeModel("عوائل"));
            typeModelList.add(new TypeDataModel.TypeModel("عزاب"));

            garageModelList.add(new TypeDataModel.TypeModel("اختر مدخل السيارات"));
            garageModelList.add(new TypeDataModel.TypeModel("يوجد"));
            garageModelList.add(new TypeDataModel.TypeModel("لا يوجد"));

            beriodModelList.add(new TypeDataModel.TypeModel("اختر مدة الايجار"));
            beriodModelList.add(new TypeDataModel.TypeModel("سنه"));
            beriodModelList.add(new TypeDataModel.TypeModel("سنتين"));

            kithchenModelList.add(new TypeDataModel.TypeModel("يوجد مطبخ ؟"));
            kithchenModelList.add(new TypeDataModel.TypeModel("نعم"));
            kithchenModelList.add(new TypeDataModel.TypeModel("لا"));

            salonnModelList.add(new TypeDataModel.TypeModel("اختر عدد الصالات"));
            salonnModelList.add(new TypeDataModel.TypeModel("1"));
            salonnModelList.add(new TypeDataModel.TypeModel("2"));
            salonnModelList.add(new TypeDataModel.TypeModel("3"));

            numroomModelList.add(new TypeDataModel.TypeModel("اختر عدد الغرف"));
            numroomModelList.add(new TypeDataModel.TypeModel("1"));
            numroomModelList.add(new TypeDataModel.TypeModel("2"));
            numroomModelList.add(new TypeDataModel.TypeModel("3"));

            leftModelList.add(new TypeDataModel.TypeModel("يوجد مصعد ؟"));
            numroomModelList.add(new TypeDataModel.TypeModel("نعم"));
            numroomModelList.add(new TypeDataModel.TypeModel("لا"));



        } else {

            numbathModelList.add(new TypeDataModel.TypeModel("choose bath room num"));
            numbathModelList.add(new TypeDataModel.TypeModel("1"));
            numbathModelList.add(new TypeDataModel.TypeModel("2"));
            numbathModelList.add(new TypeDataModel.TypeModel("3"));

            advertiseModelList.add(new TypeDataModel.TypeModel("choose bath room num"));
            advertiseModelList.add(new TypeDataModel.TypeModel("sell"));
            advertiseModelList.add(new TypeDataModel.TypeModel("buy"));
            advertiseModelList.add(new TypeDataModel.TypeModel("Convert"));

            complementModelList.add(new TypeDataModel.TypeModel("choose Complement"));
            complementModelList.add(new TypeDataModel.TypeModel("Found"));
            complementModelList.add(new TypeDataModel.TypeModel("Not Found"));

            typeModelList.add(new TypeDataModel.TypeModel("Choose a family type"));
            typeModelList.add(new TypeDataModel.TypeModel("Families"));
            typeModelList.add(new TypeDataModel.TypeModel("Singles"));

            garageModelList.add(new TypeDataModel.TypeModel("Choose the car entrance"));
            garageModelList.add(new TypeDataModel.TypeModel("Found"));
            garageModelList.add(new TypeDataModel.TypeModel("Not Found"));

            beriodModelList.add(new TypeDataModel.TypeModel("Choose Beriod"));
            beriodModelList.add(new TypeDataModel.TypeModel("year"));
            beriodModelList.add(new TypeDataModel.TypeModel("2 years"));

            kithchenModelList.add(new TypeDataModel.TypeModel(" Kitchen Found"));
            kithchenModelList.add(new TypeDataModel.TypeModel("yes"));
            kithchenModelList.add(new TypeDataModel.TypeModel("No"));

            salonnModelList.add(new TypeDataModel.TypeModel("Choose Salon Num"));
            salonnModelList.add(new TypeDataModel.TypeModel("1"));
            salonnModelList.add(new TypeDataModel.TypeModel("2"));
            salonnModelList.add(new TypeDataModel.TypeModel("3"));

            numroomModelList.add(new TypeDataModel.TypeModel("Choose Room Nums"));
            numroomModelList.add(new TypeDataModel.TypeModel("1"));
            numroomModelList.add(new TypeDataModel.TypeModel("2"));
            numroomModelList.add(new TypeDataModel.TypeModel("3"));

            leftModelList.add(new TypeDataModel.TypeModel("Left Found ?"));
            numroomModelList.add(new TypeDataModel.TypeModel("yes"));
            numroomModelList.add(new TypeDataModel.TypeModel("No"));

        }


    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        urlList = new ArrayList<>();
        numbathModelList = new ArrayList<>();
        advertiseModelList = new ArrayList<>();
        complementModelList = new ArrayList<>();
        typeModelList = new ArrayList<>();
        garageModelList = new ArrayList<>();
        beriodModelList = new ArrayList<>();
        kithchenModelList = new ArrayList<>();
        salonnModelList = new ArrayList<>();
        numroomModelList = new ArrayList<>();
        leftModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

        binding.imageSelectPhoto.setOnClickListener(view -> CreateImageAlertDialog());

        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        binding.recView.setLayoutManager(manager);
        imagesAdapter = new ImagesAdapter(urlList, this);
        binding.recView.setAdapter(imagesAdapter);
        numbathAdapter = new Spinner_Type_Adapter(numbathModelList, this);
        advertiseAdapter = new Spinner_Type_Adapter(advertiseModelList, this);
        complementAdapter = new Spinner_Type_Adapter(complementModelList, this);
        typeAdapter = new Spinner_Type_Adapter(typeModelList, this);
        garageAdapter = new Spinner_Type_Adapter(garageModelList, this);
        beriodAdapter = new Spinner_Type_Adapter(beriodModelList, this);
kitchenAdapter=new Spinner_Type_Adapter(kithchenModelList,this);
salonAdapter=new Spinner_Type_Adapter(salonnModelList,this);
numroomAdapter=new Spinner_Type_Adapter(numroomModelList,this);
leftAdapter=new Spinner_Type_Adapter(leftModelList,this);
        binding.spinnerbathnum.setAdapter(numbathAdapter);
binding.spinnertypeAdvertise.setAdapter(advertiseAdapter);
binding.spinnertypeComplement.setAdapter(complementAdapter);
binding.spinnertypeSakn.setAdapter(typeAdapter);
binding.spinnergarage.setAdapter(garageAdapter);
binding.spinneraqaarBeriod.setAdapter(beriodAdapter);
binding.spinnerkitchen.setAdapter(kitchenAdapter);
binding.spinnersalon.setAdapter(salonAdapter);
binding.spinnernumroom.setAdapter(numbathAdapter);
binding.spinnerleft.setAdapter(leftAdapter);
        binding.spinnerbathnum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setNum_bathroom("");

                } else {
                    order_upload_model.setNum_bathroom(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnertypeAdvertise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setType_advertise("");

                } else {
                    order_upload_model.setType_advertise(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnertypeComplement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setComplement("");

                } else {
                    order_upload_model.setComplement(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnertypeSakn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setType_sakn("");

                } else {
                    order_upload_model.setType_sakn(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnergarage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setGarag("");

                } else {
                    order_upload_model.setGarag(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinneraqaarBeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setAqar_peroid("");

                } else {
                    order_upload_model.setAqar_peroid(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerkitchen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setKitchen("");

                } else {
                    order_upload_model.setKitchen(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnersalon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setSalon("");

                } else {
                    order_upload_model.setSalon(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnernumroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setNum_rooms("");

                } else {
                    order_upload_model.setNum_rooms(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerleft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    order_upload_model.setLeft("");

                } else {
                    order_upload_model.setLeft(i+"");



                }
                //  Log.e("cc",city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add2Activity.this, MapActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnSend.setOnClickListener(view -> {
            if (order_upload_model.isDataValidStep1(this)) {
                if (userModel != null) {
                    if (urlList != null && urlList.size() > 0) {
                        sendorderWithImage(order_upload_model);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.add_photo), Toast.LENGTH_LONG).show();
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
            MultipartBody.Part part = Common.getMultiPart(Add2Activity.this, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }


    private void sendorderWithImage(Order2_Upload_Model order_upload_model) {
        final Dialog dialog = Common.createProgressDialog(Add2Activity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        //  Log.e("data",userModel.getUser().getId()+" "+order_upload_model.getCategory_id()+" "+order_upload_model.getSubcategory_id()+" "+order_upload_model.getCity_id()+" "+type_id+" "+order_upload_model.getTitle()+" "+order_upload_model.getDetails()+" "+order_upload_model.getAddress()+" "+order_upload_model.getLongitude()+" "+order_upload_model.getLatitude()+" "+views_num+" "+is_Special+" "+is_Install+" "+commented);
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");

      RequestBody cat_part=Common.getRequestBodyText(cat_id);
        RequestBody city_part=Common.getRequestBodyText(cat_id);

        RequestBody title_part=Common.getRequestBodyText(order_upload_model.getTitle());
        RequestBody price_part=Common.getRequestBodyText(order_upload_model.getMetr_price());
        RequestBody piceto_part=Common.getRequestBodyText(order_upload_model.getMetr_price_to());
        RequestBody size_part=Common.getRequestBodyText(order_upload_model.getAqar_size());
        RequestBody sizeto_part=Common.getRequestBodyText(order_upload_model.getAqar_size_to());

        RequestBody addres_part=Common.getRequestBodyText(order_upload_model.getAddress());

        RequestBody long_part=Common.getRequestBodyText(order_upload_model.getLongitude());
        RequestBody lat_part=Common.getRequestBodyText(order_upload_model.getLatitude());
        RequestBody typeaqa_part=Common.getRequestBodyText(order_upload_model.getType_aqar());

        RequestBody numbath_part=Common.getRequestBodyText(order_upload_model.getNum_bathroom());

        RequestBody advertise_part=Common.getRequestBodyText(order_upload_model.getType_advertise());

        RequestBody complemnet_part=Common.getRequestBodyText(order_upload_model.getComplement());

        RequestBody typeskan_part=Common.getRequestBodyText(order_upload_model.getType_sakn());
        RequestBody typegarag_part=Common.getRequestBodyText(order_upload_model.getGarag());
        RequestBody beriod_part=Common.getRequestBodyText(order_upload_model.getAqar_peroid());
        RequestBody kitchen_part=Common.getRequestBodyText(order_upload_model.getKitchen());
        RequestBody salon_part=Common.getRequestBodyText(order_upload_model.getSalon());
        RequestBody room_part=Common.getRequestBodyText(order_upload_model.getNum_rooms());
        RequestBody left_part=Common.getRequestBodyText(order_upload_model.getLeft());
        RequestBody detials_part=Common.getRequestBodyText(order_upload_model.getAqar_text());

        List<MultipartBody.Part> partimageList = getMultipartBodyList(urlList, "image[]");
        try {
            Api.getService(Tags.base_url)
                    .Sendorder2(user_part, cat_part, city_part,typeskan_part,typeaqa_part , title_part, detials_part, price_part,piceto_part, addres_part, long_part, lat_part, advertise_part, complemnet_part,typegarag_part ,beriod_part,numbath_part,salon_part,room_part,kitchen_part,left_part,size_part,sizeto_part, partimageList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(Add2Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        //  Intent intent = new Intent(AddAdsActivity.this, AddAdsActivity.class);

                        //startActivity(intent);
                        finish();
                    } else {

                        try {

                            Toast.makeText(Add2Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody().string() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(Add2Activity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
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
