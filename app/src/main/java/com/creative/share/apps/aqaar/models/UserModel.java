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

    private List<AdModel> data;

    public List<AdModel> getData() {
        return data;
    }

}
