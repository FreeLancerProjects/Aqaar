package com.creative.share.apps.aqaar.ui_general_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.share.TimeAgo;
import com.creative.share.apps.aqaar.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UI_General_Method {

    @BindingAdapter("error")
    public static void setErrorUi(View view, String error)
    {
        if (view instanceof EditText)
        {
            EditText editText = (EditText) view;
            editText.setError(error);

        }else if (view instanceof TextView)
        {
            TextView textView = (TextView) view;
            textView.setError(error);

        }
    }



    @BindingAdapter("avatarUser")
    public static void avatarUser(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_AVATAR+endPoint)).placeholder(R.drawable.ic_user).fit().into(imageView);
            }else
            {
                Picasso.with(imageView.getContext()).load(R.drawable.ic_user).fit().into(imageView);

            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_AVATAR+endPoint)).fit().into(imageView);
            }else
                {
                    Log.e("fff","fff");
                    Picasso.with(imageView.getContext()).load(R.drawable.ic_user).fit().into(imageView);

                }
        }

    }



    @BindingAdapter({"date"})
    public static void displayTime(TextView textView,long time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String sTime = dateFormat.format(new Date(time*1000));
        textView.setText(sTime);
    }
    @BindingAdapter("dateago")
    public static void convertToNotDate(TextView textView,long date)
    {
        long d = date*1000;
        String n_date = TimeAgo.getTimeAgo(d,textView.getContext());
        textView.setText(textView.getContext().getResources().getString(R.string.last_show)+n_date);

    }

    @BindingAdapter("url")
    public static void imageUrl(RoundedImageView imageView, String url)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).fit().into(imageView);

    }







}
