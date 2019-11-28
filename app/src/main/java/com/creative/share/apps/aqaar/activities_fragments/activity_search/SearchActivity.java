package com.creative.share.apps.aqaar.activities_fragments.activity_search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.databinding.ActivitySearchBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class SearchActivity extends AppCompatActivity implements Listeners.BackListener{
    private ActivitySearchBinding binding;
    private String lang;





    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        initView();
    }



    private void initView()
    {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.btnSearch.setOnClickListener(view ->
        {
            String query = binding.edtSearch.getText().toString().trim();

            if (query.isEmpty())
            {
                binding.edtSearch.setError(getString(R.string.field_req));
            }else
                {
                    binding.edtSearch.setError(null);
                    Common.CloseKeyBoard(this,binding.edtSearch);
                    search(query);

                }
        });
    }

    private void search(String query) {
        binding.progBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void back() {
        finish();
    }

}
