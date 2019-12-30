package com.creative.share.apps.aqaar.models;

import java.io.Serializable;
import java.util.List;

public class TypeDataModel implements Serializable {

    private List<TypeModel> data;

    public List<TypeModel> getData() {
        return data;
    }

    public static class TypeModel implements Serializable
    {
       private int id;
               private String main_cat_id_fk;
        private String title;
        private String details;
        private String price;
        private String img;
        private String date;
        private String date_s;
        private String publisher;

        public TypeModel(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public String getMain_cat_id_fk() {
            return main_cat_id_fk;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }

        public String getPrice() {
            return price;
        }

        public String getImg() {
            return img;
        }

        public String getDate() {
            return date;
        }

        public String getDate_s() {
            return date_s;
        }

        public String getPublisher() {
            return publisher;
        }
    }
}
