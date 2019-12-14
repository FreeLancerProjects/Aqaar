package com.creative.share.apps.aqaar.activities_fragments.chat_activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.adapters.Chat_Adapter;
import com.creative.share.apps.aqaar.databinding.ActivityChatBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.AllMessageModel;
import com.creative.share.apps.aqaar.models.MessageModel;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatBinding binding;
    private String lang;
    private Chat_Adapter chat_adapter;
    private List<MessageModel.SingleMessageModel> messagedatalist;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private String reciver_id = "0",reciver_name,phone;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initView();
        getmessge();

    }

    private void initView() {
       getdataintent();
        messagedatalist = new ArrayList<>();

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setName(reciver_name);
        binding.setBackListener(this);
        manager = new LinearLayoutManager(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
        chat_adapter = new Chat_Adapter(messagedatalist, userModel.getId(), this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        // binding.llMsgContainer.setVisibility(View.GONE);
        binding.recView.setAdapter(chat_adapter);
        binding.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });


    }

    private void getdataintent() {
        if (getIntent().getStringExtra("data") != null) {
            reciver_id = getIntent().getStringExtra("data");
        }
        if (getIntent().getStringExtra("name") != null) {
            reciver_name = getIntent().getStringExtra("name");
        }
        if (getIntent().getStringExtra("phone") != null) {
            phone = getIntent().getStringExtra("phone");
        }
    }

    private void checkdata() {
        String message = binding.edtMsgContent.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            Common.CloseKeyBoard(this, binding.edtMsgContent);
            binding.edtMsgContent.setText("");
            sendmessagetext(message);

        } else {
            binding.edtMsgContent.setError(getResources().getString(R.string.field_req));
        }
    }

    public void getmessge() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        Log.e("lkk", reciver_id + " " + userModel.getId());
        messagedatalist.clear();
        chat_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .getMessge(reciver_id, userModel.getId() + "")
                    .enqueue(new Callback<AllMessageModel>() {
                        @Override
                        public void onResponse(Call<AllMessageModel> call, Response<AllMessageModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                           //  binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                messagedatalist.clear();
                                messagedatalist.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    // binding.llMsgContainer.setVisibility(View.GONE);
                                    chat_adapter.notifyDataSetChanged();
                                    binding.recView.scrollToPosition(messagedatalist.size() - 1);

                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    chat_adapter.notifyDataSetChanged();

                                    //   binding.llNoStore.setVisibility(View.VISIBLE);

                                }
                            } else {

                                chat_adapter.notifyDataSetChanged();

                                //binding.llNoStore.setVisibility(View.VISIBLE);
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AllMessageModel> call, Throwable t) {
                            try {
                                //binding.swipeRefresh.setRefreshing(false);

                                binding.progBar.setVisibility(View.GONE);
                                //binding.llNoStore.setVisibility(View.VISIBLE);


                                Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            //binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }


    private void sendmessagetext(String message) {
        final Dialog dialog = Common.createProgressDialog(ChatActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {
            Api.getService(Tags.base_url)
                    .sendmessagetext(userModel.getId() + "", reciver_id, message).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {

                        Log.e("llll", response.toString());

                        messagedatalist.add(response.body().getData());
                        chat_adapter.notifyDataSetChanged();
                        binding.recView.scrollToPosition(messagedatalist.size() - 1);
                    } else {
                        try {

                            Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
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


    @Override
    public void back() {
        finish();
    }

}
