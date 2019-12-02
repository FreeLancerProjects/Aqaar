package com.creative.share.apps.aqaar.models;

import java.io.Serializable;

public class App_Data_Model implements Serializable {
  private int id;
          private String en_title;
    private String ar_title;
    private String ar_content;
    private String en_content;
    private String image;

    public int getId() {
        return id;
    }

    public String getEn_title() {
        return en_title;
    }

    public String getAr_title() {
        return ar_title;
    }

    public String getAr_content() {
        return ar_content;
    }

    public String getEn_content() {
        return en_content;
    }

    public String getImage() {
        return image;
    }
}
