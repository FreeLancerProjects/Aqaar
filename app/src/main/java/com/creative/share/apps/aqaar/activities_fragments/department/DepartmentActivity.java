package com.creative.share.apps.aqaar.activities_fragments.department;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aqaar.adapters.DepartmentAdapter;
import com.creative.share.apps.aqaar.databinding.ActivityChooseDeparmentBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
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

public class DepartmentActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChooseDeparmentBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private List<CategoryDataModel.CategoryModel> orderDataModelList;
    private Preferences preferences;
    private UserModel userModel;
    private DepartmentAdapter adapter;
    private int current_page=1;
    private boolean isLoading = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_deparment);
        initView();
    }



    private void initView() {
        orderDataModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new DepartmentAdapter(orderDataModelList,this);
        binding.recView.setAdapter(adapter);



        getDepartments();

    }



    private void getDepartments()
    {
        try {

            Api.getService(Tags.base_url)
                    .getAllCategories()
                    .enqueue(new Callback<CategoryDataModel>() {
                        @Override
                        public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                               orderDataModelList.clear();
                               orderDataModelList.addAll(response.body().getData());
                               adapter.notifyDataSetChanged();
                               if (orderDataModelList.size()>0)
                               {
                                   binding.llNoOrder.setVisibility(View.GONE);
                               }else
                                   {
                                       binding.llNoOrder.setVisibility(View.VISIBLE);

                                   }

                            }else
                            { if (response.code() == 500) {
                                    Toast.makeText(DepartmentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(DepartmentActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(DepartmentActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(DepartmentActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(DepartmentActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            Log.e("e",e.getMessage()+"__");
            binding.progBar.setVisibility(View.GONE);
        }
    }



    @Override
    public void back() {
        finish();
    }


    public void setitem(CategoryDataModel.CategoryModel s) {
        Intent intent = new Intent(this, TermsActivity.class);
        intent.putExtra("type","1");
        intent.putExtra("depart",s);
        startActivity(intent);
        finish();
    }
}
