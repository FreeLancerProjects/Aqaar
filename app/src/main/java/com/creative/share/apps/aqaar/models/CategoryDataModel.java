package com.creative.share.apps.aqaar.models;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel implements Serializable {

    private List<CategoryModel> data;

    public List<CategoryModel> getData() {
        return data;
    }

    public static class CategoryModel implements Serializable
    {
        private int id;
        private String title_ar;
        private String tite_en;


        public CategoryModel() {
        }

        public CategoryModel(int id, String title_ar, String tite_en) {
            this.id = id;
            this.title_ar = title_ar;
            this.tite_en = tite_en;
        }

        public int getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTite_en() {
            return tite_en;
        }
    }
}
