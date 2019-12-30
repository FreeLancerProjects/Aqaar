package com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.aqaar.adapters.Room_Adapter;
import com.creative.share.apps.aqaar.databinding.FragmentChatBinding;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.models.UserRoomModelData;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Chat extends Fragment{
    private FragmentChatBinding binding;

    private HomeActivity activity;
    private Preferences preferences;
    private LinearLayoutManager manager;
    private UserModel userModel;
    private List<UserRoomModelData.UserRoomModel> userRoomModels;
    private Room_Adapter room_adapter;
    private String lang;

    public static Fragment_Chat newInstance() {
        return new Fragment_Chat();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat,container,false);
        initView();
        if(userModel!=null){
            getRooms();}
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        userRoomModels=new ArrayList<>();
        room_adapter=new Room_Adapter(userRoomModels,activity,this);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setAdapter(room_adapter);



    }
    public void getRooms() {
        Api.getService(Tags.base_url)
                .getRooms(userModel.getUser().getId()+"")
                .enqueue(new Callback<UserRoomModelData>() {
                    @Override
                    public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            if (response.body().getData().size()>0)
                            {
                                userRoomModels.clear();
                                userRoomModels.addAll(response.body().getData());
                                binding.tvNoConversation.setVisibility(View.GONE);
                                room_adapter.notifyDataSetChanged();

                            }else
                            {
                                binding.tvNoConversation.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                        try {

                            binding.progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void gotomessage(int receiver_id, String receiver_name, String receiver_mobile) {
        Intent intent=new Intent(activity, ChatActivity.class);
        intent.putExtra("data",receiver_id+"");
        intent.putExtra("name",receiver_name);
        intent.putExtra("phone",receiver_mobile);

        startActivity(intent);
    }
}
