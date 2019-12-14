package com.creative.share.apps.aqaar.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.Department.DepartmentActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Ads;
import com.creative.share.apps.aqaar.databinding.AdRowBinding;
import com.creative.share.apps.aqaar.databinding.DepartmnetRowBinding;
import com.creative.share.apps.aqaar.models.AdModel;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
import com.creative.share.apps.aqaar.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.Service_Holder> {
    private final String lang;
    private List<CategoryDataModel.CategoryModel> list;
    private Context context;
private DepartmentActivity departmentActivity;
    public DepartmentAdapter(List<CategoryDataModel.CategoryModel> list, Context context) {
        this.list = list;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        departmentActivity=(DepartmentActivity)context;
    }

    @Override
    public Service_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        DepartmnetRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.departmnet_row, viewGroup, false);
        return new Service_Holder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final Service_Holder holder, final int i) {
        CategoryDataModel.CategoryModel adModel = list.get(i);
        holder.binding.setCatogryModel(adModel);
holder.binding.setLang(lang);

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        departmentActivity.setitem(list.get(holder.getLayoutPosition()).getId()+"");
    }
});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Service_Holder extends RecyclerView.ViewHolder {
        private DepartmnetRowBinding binding;

        public Service_Holder(@NonNull DepartmnetRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }
}