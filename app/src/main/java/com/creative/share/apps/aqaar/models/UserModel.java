package com.creative.share.apps.aqaar.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    private User user;

    public User getUser() {
        return user;
    }

    public class User implements Serializable {
        private int id;
        private String user_phone_code;
        private String user_phone;
        private String user_name;
        private String user_email;
        private long last_login;
        private String created_at;

        public String getCreated_at() {
            return created_at;
        }

        public int getId() {
            return id;
        }

        public String getUser_phone_code() {
            return user_phone_code;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public String getUser_name() {
            return user_name;
        }


        public String getUser_email() {
            return user_email;
        }
        public long getLast_login() {
            return last_login;
        }


    }
private List<UserModel.AdModel> ads;

    public List<AdModel> getAds() {
        return ads;
    }

    public class AdModel implements Serializable {

        private int id;
        private int aqar_type;
        private int main_cat_id_fk;
        private String city;
        private String aqar_makan;
        private String aqar_size;
        private String aqar_title;
        private double aqar_lat;
        private double aqar_long;
        private String metr_price;
        private String type_sakn;
        private String egar_peroid;
        private String num_rooms;
        private String num_salon;
        private String lift;
        private String takeef;
        private String num_bathroom;
        private String complement;
        private String kitchen;
        private String type_aqar;
        private String type_advertise;
        private String aqar_size_to;
        private String metr_price_to;
        private String garag;
        private String aqar_text;
        private String other_metr_price;
        private String other_size;
        private String other_street_width;
        private String other_interface;
        private String other_type_sakn;
        private String other_aqar_text;
        private String user_id;
        private String date_s;
        private String publisher;
        private String google_distance;
        private String user_name;
        private String image;
        private String city_name;
        private String category_name;
        private int councomments;



        public int getId() {
            return id;
        }

        public int getAqar_type() {
            return aqar_type;
        }

        public int getMain_cat_id_fk() {
            return main_cat_id_fk;
        }

        public String getCity() {
            return city;
        }

        public String getAqar_makan() {
            return aqar_makan;
        }

        public String getAqar_size() {
            return aqar_size;
        }

        public String getAqar_title() {
            return aqar_title;
        }

        public double getAqar_lat() {
            return aqar_lat;
        }

        public double getAqar_long() {
            return aqar_long;
        }

        public String getMetr_price() {
            return metr_price;
        }

        public String getType_sakn() {
            return type_sakn;
        }

        public String getEgar_peroid() {
            return egar_peroid;
        }

        public String getNum_rooms() {
            return num_rooms;
        }

        public String getNum_salon() {
            return num_salon;
        }

        public String getLift() {
            return lift;
        }

        public String getTakeef() {
            return takeef;
        }

        public String getNum_bathroom() {
            return num_bathroom;
        }

        public String getComplement() {
            return complement;
        }

        public String getKitchen() {
            return kitchen;
        }

        public String getType_aqar() {
            return type_aqar;
        }

        public String getType_advertise() {
            return type_advertise;
        }

        public String getAqar_size_to() {
            return aqar_size_to;
        }

        public String getMetr_price_to() {
            return metr_price_to;
        }

        public String getGarag() {
            return garag;
        }

        public String getAqar_text() {
            return aqar_text;
        }

        public String getOther_metr_price() {
            return other_metr_price;
        }

        public String getOther_size() {
            return other_size;
        }

        public String getOther_street_width() {
            return other_street_width;
        }

        public String getOther_interface() {
            return other_interface;
        }

        public String getOther_type_sakn() {
            return other_type_sakn;
        }

        public String getOther_aqar_text() {
            return other_aqar_text;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getDate_s() {
            return date_s;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getGoogle_distance() {
            return google_distance;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getImage() {
            return image;
        }

        public String getCity_name() {
            return city_name;
        }

        public String getCategory_name() {
            return category_name;
        }

        public int getCouncomments() {
            return councomments;
        }
    }


}
