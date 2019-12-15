package com.creative.share.apps.aqaar.models;

import java.io.Serializable;
import java.util.List;

public class CityDataModel implements Serializable {

    private List<CityModel> data;

    public List<CityModel> getData() {
        return data;
    }

    public static class CityModel implements Serializable{
        private int id_city;
        private String ar_city_title;
        private String en_city_title;
        private String province_id_fk;
        private String country_id_fk;
        private double google_latitude;
        private double google_longitude;

        public CityModel(String ar_city_title, String en_city_title) {
            this.ar_city_title = ar_city_title;
            this.en_city_title = en_city_title;
        }

        public int getId_city() {
            return id_city;
        }

        public String getAr_city_title() {
            return ar_city_title;
        }

        public String getEn_city_title() {
            return en_city_title;
        }

        public String getProvince_id_fk() {
            return province_id_fk;
        }

        public String getCountry_id_fk() {
            return country_id_fk;
        }

        public double getGoogle_latitude() {
            return google_latitude;
        }

        public double getGoogle_longitude() {
            return google_longitude;
        }
    }
}
