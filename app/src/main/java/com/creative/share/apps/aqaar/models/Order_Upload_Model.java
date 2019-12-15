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
    private String title;
    private String longitude;
    private String latitude;
    private String address;
    private String details;
    private String price;
    private String type_skan;

private String num_rooms;

    public ObservableField<String> address_error = new ObservableField<>();
    public ObservableField<String> title_error = new ObservableField<>();
    public ObservableField<String> detials_error = new ObservableField<>();


    public boolean isDataValidStep1(Context context)
    {
        if (
                !type_id.equals("")&&
                !city_id.equals("")&&
                !TextUtils.isEmpty(title)&&
                !TextUtils.isEmpty(details)



        )
        {
            address_error.set(null);
            title_error.set(null);
            detials_error.set(null);


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



                if (TextUtils.isEmpty(title))
                {
                    title_error.set(context.getString(R.string.field_req));
                }else
                {
                    title_error.set(null);

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
    public String getType_skan() {
        return type_skan;
    }

    public void setType_skan(String type_skan) {
        this.type_skan = type_skan;
        notifyPropertyChanged(BR.type_skan);

    }

    @Bindable
    public String getNum_rooms() {
        return num_rooms;
    }

    public void setNum_rooms(String num_rooms) {
        this.num_rooms = num_rooms;
        notifyPropertyChanged(BR.num_rooms);

    }
}
