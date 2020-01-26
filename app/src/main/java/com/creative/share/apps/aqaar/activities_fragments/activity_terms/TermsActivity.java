package com.creative.share.apps.aqaar.activities_fragments.activity_terms;

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

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_add2.Add1Activity;
import com.creative.share.apps.aqaar.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aqaar.databinding.ActivityTermsBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.App_Data_Model;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityTermsBinding binding;
    private String lang;
    private String type;
    private CategoryDataModel.CategoryModel depart_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms);
        initView();
getdatafromintent();

    }

    private void getdatafromintent() {
        if(getIntent().getStringExtra("type")!=null){
            type=getIntent().getStringExtra("type");
            if(type.equals("1")){
                depart_id= (CategoryDataModel.CategoryModel) getIntent().getSerializableExtra("depart");
            }
            else {
                binding.btnapplay.setVisibility(View.GONE);

            }
        }
    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        getTerms();
binding.btnapplay.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(depart_id.getId()==1){
        Intent intent = new Intent(TermsActivity.this, AddAdsActivity.class);
        intent.putExtra("depart",depart_id);

        startActivity(intent);
        finish();}
        else {
            Intent intent = new Intent(TermsActivity.this, Add1Activity.class);
            intent.putExtra("depart",depart_id);

            startActivity(intent);
            finish();
        }

    }
});

    }

    private void getTerms() {

        Api.getService(Tags.base_url)
                .getterms()
                .enqueue(new Callback<App_Data_Model>() {
                    @Override
                    public void onResponse(Call<App_Data_Model> call, Response<App_Data_Model> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {

                            binding.setAppdatamodel(response.body());
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                              //  Toast.makeText(TermsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                //Toast.makeText(TermsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<App_Data_Model> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                  //  Toast.makeText(TermsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(TermsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    @Override
    public void back() {
        finish();
    }
}
