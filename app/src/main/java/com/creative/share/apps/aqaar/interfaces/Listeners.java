package com.creative.share.apps.aqaar.interfaces;


import com.creative.share.apps.aqaar.models.ContactUsModel;

public interface Listeners {


    interface LoginListener {
        void checkDataLogin();
    }
    interface SkipListener
    {
        void skip();
    }
    interface BackListener
    {
        void back();
    }
    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener {
        void checkDataSignUp();
    }

    interface ContactListener
    {
        void sendContact(ContactUsModel contactUsModel);
    }
}
