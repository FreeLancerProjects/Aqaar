package com.creative.share.apps.aqaar.models;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.creative.share.apps.aqaar.R;


public class SignUpModel extends BaseObservable {

    private String user_name;
    private String user_full_name;

    private String email;
    private String phone_code;
    private String phone;
    private String password;



    public ObservableField<String> error_user_name = new ObservableField<>();
    public ObservableField<String> error_user_full_name = new ObservableField<>();

    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public SignUpModel(String user_full_name,String user_name, String phone_code, String phone,String email, String password) {
        setUser_name(user_name);
        setUser_full_name(user_full_name);
        setPhone_code(phone_code);
        setPhone(phone);
        setEmail(email);
        setPassword(password);

    }

    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(user_name)&&
                !TextUtils.isEmpty(user_full_name)&&
                !TextUtils.isEmpty(email)&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                !TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone)&&
                password.length()>=6
        )
        {
            error_user_name.set(null);
            error_user_full_name.set(null);
            error_email.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
            error_password.set(null);

            return true;
        }else
        {

            if (user_name.isEmpty())
            {
                error_user_name.set(context.getString(R.string.field_req));
            }else
            {
                error_user_name.set(null);
            }
            if(user_full_name.isEmpty()){
                error_user_full_name.set(context.getResources().getString(R.string.field_req));
            }

            if (email.isEmpty())
            {
                error_email.set(context.getString(R.string.field_req));

            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                error_email.set(context.getString(R.string.inv_email));
            }
            else
            {
                error_email.set(null);
            }

            if (phone_code.isEmpty())
            {
                error_phone_code.set(context.getString(R.string.field_req));
            }else
            {
                error_phone_code.set(null);
            }

            if (phone.isEmpty())
            {
                error_phone.set(context.getString(R.string.field_req));
            }else
            {
                error_phone.set(null);
            }

            if (password.isEmpty())
            {
                error_password.set(context.getString(R.string.field_req));
            }else if (password.length()<6)
            {
                error_password.set(context.getString(R.string.pass_short));
            }else
            {
                error_password.set(null);
            }





            return false;
        }
    }

    public SignUpModel() {
        this.phone_code = "";
        notifyPropertyChanged(BR.phone_code);
        this.phone="";
        notifyPropertyChanged(BR.phone);
        this.password = "";
        notifyPropertyChanged(BR.password);
        this.user_name = "";
        notifyPropertyChanged(BR.user_name);
        this.user_full_name="";
        notifyPropertyChanged(BR.user_full_name);

        this.email = "";
        notifyPropertyChanged(BR.email);




    }
    @Bindable
    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
        notifyPropertyChanged(BR.user_full_name);

    }

    @Bindable
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
        notifyPropertyChanged(BR.user_name);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }
    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }




}
