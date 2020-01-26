package com.creative.share.apps.aqaar.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aqaar.databinding.FragmentSignInBinding;
import com.creative.share.apps.aqaar.interfaces.Listeners;
import com.creative.share.apps.aqaar.models.LoginModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.share.Common;
import com.creative.share.apps.aqaar.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener,Listeners.CreateAccountListener,Listeners.SkipListener,Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String current_language;
    private Preferences preferences;
    private CountryPicker countryPicker;
    private LoginModel loginModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        loginModel = new LoginModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLoginModel(loginModel);
        binding.setLang(current_language);
        binding.setLoginListener(this);
        binding.setNewAccountListener(this);
        binding.setSkipListener(this);
        binding.setShowDialogListener(this);
        createCountryDialog();




    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM()!=null)
        {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        }else if (telephonyManager!=null&&countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        }else if (countryPicker.getCountryByLocale(Locale.getDefault())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        }else
        {
            String code = "+966";
            binding.tvCode.setText(code);
            loginModel.setPhone_code(code.replace("+","00"));

        }

    }


    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }


    @Override
    public void checkDataLogin(String phone_code, String phone, String password) {
      /*  if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }*/
        loginModel = new LoginModel(phone_code,phone,password);
        binding.setLoginModel(loginModel);

        if (loginModel.isDataValid(activity))
        {
            if(phone.startsWith("0")){
                phone=phone.replaceFirst("0","");

            }

            phone_code=phone_code.replace("+","00");


            login(phone_code,phone,password);
        }
    }

    private void login(String phone_code, String phone, String password)
    {
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .login(phone,phone_code,password)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                             // activity.displayFragmentCodeVerification(response.body());
                                preferences.create_update_userData(activity,response.body());
                                preferences.createSession(activity, Tags.session_login);
                                Intent intent = new Intent(activity,HomeActivity.class);
                                startActivity(intent);
                                activity.finish();
                            }else
                            {
                               /* try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
                                if (response.code() == 422) {
                                   // Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                   // userModel.setUser(new UserModel.User().setId());
                                    //response.body().setUser(new UserModel.User());
                                    //response.body().getUser().setId(response.body().getUser_id());
                                    //
                                    try {

                                        JSONObject obj = null;

                                        try {
                                            String re=response.errorBody().string();
                                            Log.e("data",re);
                                            obj = new JSONObject(re);
                                           // Log.e("data",obj.stri);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("data",e.getMessage());
                                        }
                                        UserModel userModel=new UserModel();

                                        userModel.setUser(new UserModel.User());
                                        Log.e("data",obj.toString());

                                        Log.e("data",obj.get("user_id").toString());
                                        userModel.getUser().setId(Integer.parseInt(obj.get("user_id").toString()));
                                        activity.displayFragmentCodeVerification(userModel);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    //    Log.e("data",e.getMessage());
                                    }

                                    //  Log.e("error",response.code()+"_"+response.errorBody()+response.message()+password+phone+phone_code);

                                }else if (response.code()==404)
                                {
                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else if (response.code() == 500) {
                    //                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                }else
                                {
                      //              Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                        //                Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                          //              Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();

        }
    }

    @Override
    public void skip() {
        navigateToHomeActivity();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();

    }

    @Override
    public void createNewAccount() {
        activity.DisplayFragmentSignUp();
    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(activity);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        loginModel.setPhone_code(country.getDialCode().replace("+","00"));

    }
}
