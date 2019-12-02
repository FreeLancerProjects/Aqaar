package com.creative.share.apps.aqaar.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int user_id;
    private String phone_code;
    private String user_phone;
    private String user_name;
    private String user_username;
    private String user_email;
    private int user_type;
    private String user_photo;
    private int is_active;
    private int admin_type;
    private long last_login;

    public int getUser_id() {
        return user_id;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_username() {
        return user_username;
    }

    public String getUser_email() {
        return user_email;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public int getIs_active() {
        return is_active;
    }

    public int getAdmin_type() {
        return admin_type;
    }

    public long getLast_login() {
        return last_login;
    }
}
