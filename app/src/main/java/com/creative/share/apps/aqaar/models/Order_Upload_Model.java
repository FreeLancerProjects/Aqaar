package com.creative.share.apps.aqaar.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.aqaar.BR;
import com.creative.share.apps.aqaar.R;

import java.io.Serializable;


public class Order_Upload_Model extends BaseObservable implements Serializable {
    private String type_id;
    private String city_id;
    private String other_interface;
private String title;
    private String longitude;
    private String latitude;
    private String address;
    private String details;
    private String price;
    private String other_size;

private String other_street_width;

    public ObservableField<String> address_error = new ObservableField<>();
    public ObservableField<String> other_interface_error = new ObservableField<>();
    public ObservableField<String> title_error = new ObservableField<>();
    public ObservableField<String> price_error = new ObservableField<>();

    public ObservableField<String> detials_error = new ObservableField<>();
    public ObservableField<String> other_size_error = new ObservableField<>();
    public ObservableField<String> other_street_width_error = new ObservableField<>();


    public boolean isDataValidStep1(Context context)
    {
        if (
                !type_id.equals("")&&
                !city_id.equals("")&&
                !TextUtils.isEmpty(other_interface)&&
                !TextUtils.isEmpty(details)&&
                        !TextUtils.isEmpty(other_size)
&&!TextUtils.isEmpty(title)
                        &&!TextUtils.isEmpty(address)

                        &&!TextUtils.isEmpty(other_street_width)&&!TextUtils.isEmpty(price)

        )
        {
            address_error.set(null);
            other_interface_error.set(null);
            detials_error.set(null);
other_size_error.set(null);
other_street_width_error.set(null);
title_error.set(null);
price_error.set(null);
            return true;
        }else
            {


                if (type_id==null||type_id.equals(""))
                {
                    Toast.makeText(context, R.string.Choose_aqaar_type, Toast.LENGTH_SHORT).show();
                }

                if (city_id==null||city_id.equals(""))
                {
                    Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(address))
                {
                    address_error.set(context.getString(R.string.field_req));
                }else
                {
                    address_error.set(null);

                }

                if (TextUtils.isEmpty(other_interface))
                {
                    other_interface_error.set(context.getString(R.string.field_req));
                }else
                {
                    other_interface_error.set(null);

                }
                if (TextUtils.isEmpty(price))
                {
                    price_error.set(context.getString(R.string.field_req));
                }else
                {
                    other_interface_error.set(null);

                }
                if (TextUtils.isEmpty(title))
                {
                    title_error.set(context.getString(R.string.field_req));
                }else
                {
                    title_error.set(null);

                }
                if (TextUtils.isEmpty(other_size))
                {
                    other_size_error.set(context.getString(R.string.field_req));
                }else
                {
                    other_size_error.set(null);

                }
                if (TextUtils.isEmpty(other_street_width))
                {
                    other_street_width_error.set(context.getString(R.string.field_req));
                }else
                {
                    other_street_width_error.set(null);

                }
                if (TextUtils.isEmpty(details))
                {
                    detials_error.set(context.getString(R.string.field_req));
                }else
                {
                    detials_error.set(null);

                }

                return false;
            }
    }







    @Bindable
    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
        notifyPropertyChanged(BR.type_id);
    }

    @Bindable
    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
        notifyPropertyChanged(BR.city_id);

    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);

    }




    @Bindable
    public String getOther_interface() {
        return other_interface;
    }

    public void setOther_interface(String other_interface) {
        this.other_interface = other_interface;
        notifyPropertyChanged(BR.other_interface);

    }
    @Bindable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.latitude);

    }

    @Bindable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }
    @Bindable
    public String getOther_size() {
        return other_size;
    }

    public void setOther_size(String other_size) {
        this.other_size = other_size;
        notifyPropertyChanged(BR.other_size);

    }

    @Bindable
    public String getOther_street_width() {
        return other_street_width;
    }

    public void setOther_street_width(String other_street_width) {
        this.other_street_width = other_street_width;
        notifyPropertyChanged(BR.other_street_width);

    }
}
