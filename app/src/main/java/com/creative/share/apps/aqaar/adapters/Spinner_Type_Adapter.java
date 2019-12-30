package com.creative.share.apps.aqaar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.databinding.SpinnerTypeRowBinding;
import com.creative.share.apps.aqaar.models.TypeDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Spinner_Type_Adapter extends BaseAdapter {
    private List<TypeDataModel.TypeModel> dataList;
    private Context context;
    private String lang;
    public Spinner_Type_Adapter(List<TypeDataModel.TypeModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerTypeRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_type_row,viewGroup,false);
        binding.setLang(lang);
        binding.setTypemodel(dataList.get(i));
        return binding.getRoot();
    }
}
