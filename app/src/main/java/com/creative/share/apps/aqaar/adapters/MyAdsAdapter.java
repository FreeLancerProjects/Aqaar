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
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Ads;
import com.creative.share.apps.aqaar.databinding.AdRowBinding;
import com.creative.share.apps.aqaar.models.AdModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdsAdapter extends RecyclerView.Adapter<MyAdsAdapter.Service_Holder> {
    private List<UserModel.AdModel> list;
    private Context context;
    private Fragment_Main_Ads fragment_main_ads;

    public MyAdsAdapter(List<UserModel.AdModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public Service_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        AdRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.ad_row, viewGroup, false);
        return new Service_Holder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final Service_Holder holder, final int i) {
        UserModel.AdModel adModel = list.get(i);

        try {
            if (adModel.getAqar_title()!=null&&!adModel.getAqar_title().isEmpty())
            {
                holder.binding.tvTitle.setText(adModel.getAqar_title());

            }else
            {
                holder.binding.tvTitle.setText(context.getString(R.string.no_name));

            }

            if (adModel.getMain_cat_id_fk()==1)
            {

                if (adModel.getOther_metr_price()!=null&&!adModel.getOther_metr_price().isEmpty())
                {
                    holder.binding.tvPrice.setText(String.format("%s %s",adModel.getOther_metr_price(),context.getString(R.string.sar)));

                }else
                {
                    holder.binding.tvPrice.setText(context.getString(R.string.no_price));

                }

                if (adModel.getOther_aqar_text()!=null&&!adModel.getOther_aqar_text().isEmpty())
                {
                    holder.binding.tvDetails.setText(adModel.getOther_aqar_text());

                }else
                {
                    holder.binding.tvDetails.setText(context.getString(R.string.no_details));

                }

            }else
                {
                    if (adModel.getMetr_price()!=null&&!adModel.getMetr_price().isEmpty())
                    {
                        holder.binding.tvPrice.setText(String.format("%s %s",adModel.getMetr_price(),context.getString(R.string.sar)));

                    }else
                    {
                        holder.binding.tvPrice.setText(context.getString(R.string.no_price));

                    }

                    if (adModel.getAqar_text()!=null&&!adModel.getAqar_text().isEmpty())
                    {
                        holder.binding.tvDetails.setText(adModel.getAqar_text());

                    }else
                    {
                        holder.binding.tvDetails.setText(context.getString(R.string.no_details));

                    }
                }





            if (adModel.getAqar_makan()!=null&&!adModel.getAqar_makan().isEmpty())
            {
                holder.binding.tvAddress.setText(String.format("%s %s",adModel.getAqar_makan(),adModel.getCity_name()));

            }else
            {
                holder.binding.tvAddress.setText(adModel.getCity_name());

            }


            Picasso.with(context).load(Uri.parse(Tags.base_url+adModel.getImage())).fit().into(holder.binding.image, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    holder.binding.progBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.binding.progBar.setVisibility(View.GONE);

                }
            });

        }catch (Exception e)
        {
            if (e!=null&&e.getMessage()!=null)
            {
                Log.e("error",e.getMessage());
            }

        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Service_Holder extends RecyclerView.ViewHolder {
        private AdRowBinding binding;

        public Service_Holder(@NonNull AdRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }
}