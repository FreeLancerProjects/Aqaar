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


public class Order2_Upload_Model extends BaseObservable implements Serializable {
    private String city_id;
    private String title;
    private String metr_price;
    private String metr_price_to;
    private String aqar_size;
    private String aqar_size_to;
    private String type_aqar;
    private String num_bathroom;
    private String type_advertise;
    private String complement;
    private String type_sakn;
    private String garag;
    private String aqar_peroid;
    private String num_rooms;
    private String kitchen;
    private String salon;
    private String aqar_text;
    private String left;
    private String longitude;
    private String latitude;
    private String address;
    public ObservableField<String> address_error = new ObservableField<>();
    public ObservableField<String> title_error = new ObservableField<>();
    public ObservableField<String> metr_price_error = new ObservableField<>();

    public ObservableField<String> metr_price_to_error = new ObservableField<>();
    public ObservableField<String> aqar_size_error = new ObservableField<>();
    public ObservableField<String> aqar_size_to_error = new ObservableField<>();
    public ObservableField<String> aqar_text_error = new ObservableField<>();

    public boolean isDataValidStep1(Context context) {
        if (
                !type_aqar.equals("") &&
                        !city_id.equals("") &&
                        !TextUtils.isEmpty(title) &&
                        !TextUtils.isEmpty(metr_price) &&
                        !TextUtils.isEmpty(metr_price_to)
                        && !TextUtils.isEmpty(aqar_size)
                        && !TextUtils.isEmpty(aqar_size_to) && !TextUtils.isEmpty(aqar_text)

        ) {
            title_error.set(null);
            metr_price_error.set(null);
            metr_price_to_error.set(null);
            aqar_size_error.set(null);
            aqar_size_to_error.set(null);
            aqar_text_error.set(null);
            return true;
        } else {


            if (type_aqar == null || type_aqar.equals("")) {
                Toast.makeText(context, R.string.Choose_aqaar_type, Toast.LENGTH_SHORT).show();
            }

            if (city_id == null || city_id.equals("")) {
                Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();
            }


            if (TextUtils.isEmpty(metr_price)) {
                metr_price_error.set(context.getString(R.string.field_req));
            } else {
                metr_price_error.set(null);

            }
            if (TextUtils.isEmpty(metr_price_to)) {
                metr_price_to_error.set(context.getString(R.string.field_req));
            } else {
                metr_price_to_error.set(null);

            }
            if (TextUtils.isEmpty(title)) {
                title_error.set(context.getString(R.string.field_req));
            } else {
                title_error.set(null);

            }
            if (TextUtils.isEmpty(aqar_size)) {
                aqar_size_error.set(context.getString(R.string.field_req));
            } else {
                aqar_size_error.set(null);

            }
            if (TextUtils.isEmpty(aqar_size_to)) {
                aqar_size_to_error.set(context.getString(R.string.field_req));
            } else {
                aqar_size_to_error.set(null);

            }
            if (TextUtils.isEmpty(aqar_text)) {
                aqar_text_error.set(context.getString(R.string.field_req));
            } else {
                aqar_text_error.set(null);

            }

            return false;
        }
    }

    public boolean isDataValidStep2(Context context) {
        if (
                !num_bathroom.equals("") &&
                        !type_advertise.equals("") &&
                        !complement.equals("") &&
                        !type_sakn.equals("") &&
                        !garag.equals("") &&
                        !aqar_peroid.equals("") &&
                        !kitchen.equals("") &&
                        !salon.equals("") &&
                        !num_rooms.equals("")
                        &&!left.equals("")

                        && !TextUtils.isEmpty(address)

        ) {

            return true;
        } else {


            if (num_bathroom == null || num_bathroom.equals("")) {
                Toast.makeText(context, R.string.choose_num_bathroom, Toast.LENGTH_SHORT).show();
            }
            if (type_advertise == null || type_advertise.equals("")) {
                Toast.makeText(context, R.string.choose_advertise_type, Toast.LENGTH_SHORT).show();
            }
            if (complement == null || complement.equals("")) {
                Toast.makeText(context, R.string.choose_complement, Toast.LENGTH_SHORT).show();
            }
            if (type_sakn == null || type_sakn.equals("")) {
                Toast.makeText(context, R.string.choose_type_sakn, Toast.LENGTH_SHORT).show();
            }
            if (garag == null || garag.equals("")) {
                Toast.makeText(context, R.string.choose_garag, Toast.LENGTH_SHORT).show();
            }
            if (aqar_peroid == null || aqar_peroid.equals("")) {
                Toast.makeText(context, R.string.choose_aqar_peroid, Toast.LENGTH_SHORT).show();
            }
            if (kitchen == null || kitchen.equals("")) {
                Toast.makeText(context, R.string.choose_kitchen, Toast.LENGTH_SHORT).show();
            }
            if (salon == null || salon.equals("")) {
                Toast.makeText(context, R.string.choose_salon, Toast.LENGTH_SHORT).show();
            }
            if (num_rooms == null || num_rooms.equals("")) {
                Toast.makeText(context, R.string.choose_num_rooms, Toast.LENGTH_SHORT).show();
            }
            if (left == null || left.equals("")) {
                Toast.makeText(context, R.string.choose_left, Toast.LENGTH_SHORT).show();
            }

            if (city_id == null || city_id.equals("")) {
                Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();
            }



            if (TextUtils.isEmpty(address)) {
                address_error.set(context.getString(R.string.field_req));
            } else {
                address_error.set(null);

            }


            return false;
        }
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public String getMetr_price() {
        return metr_price;
    }

    public void setMetr_price(String metr_price) {
        this.metr_price = metr_price;
    }

    @Bindable
    public String getMetr_price_to() {
        return metr_price_to;
    }

    public void setMetr_price_to(String metr_price_to) {
        this.metr_price_to = metr_price_to;
    }

    @Bindable

    public String getAqar_size() {
        return aqar_size;
    }

    public void setAqar_size(String aqar_size) {
        this.aqar_size = aqar_size;
    }

    @Bindable

    public String getAqar_size_to() {
        return aqar_size_to;
    }

    public void setAqar_size_to(String aqar_size_to) {
        this.aqar_size_to = aqar_size_to;
    }

    @Bindable

    public String getType_aqar() {
        return type_aqar;
    }

    public void setType_aqar(String type_aqar) {
        this.type_aqar = type_aqar;
    }

    @Bindable

    public String getNum_bathroom() {
        return num_bathroom;
    }

    public void setNum_bathroom(String num_bathroom) {
        this.num_bathroom = num_bathroom;
    }

    @Bindable

    public String getType_advertise() {
        return type_advertise;
    }

    public void setType_advertise(String type_advertise) {
        this.type_advertise = type_advertise;
    }

    @Bindable

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    @Bindable

    public String getType_sakn() {
        return type_sakn;
    }

    public void setType_sakn(String type_sakn) {
        this.type_sakn = type_sakn;
    }

    @Bindable

    public String getGarag() {
        return garag;
    }

    public void setGarag(String garag) {
        this.garag = garag;
    }

    @Bindable

    public String getAqar_peroid() {
        return aqar_peroid;
    }

    public void setAqar_peroid(String aqar_peroid) {
        this.aqar_peroid = aqar_peroid;
    }

    @Bindable

    public String getNum_rooms() {
        return num_rooms;
    }

    public void setNum_rooms(String num_rooms) {
        this.num_rooms = num_rooms;
    }

    @Bindable

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    @Bindable

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    @Bindable

    public String getAqar_text() {
        return aqar_text;
    }

    public void setAqar_text(String aqar_text) {
        this.aqar_text = aqar_text;
    }

    @Bindable

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    @Bindable

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Bindable

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Bindable

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
