package com.creative.share.apps.aqaar.activities_fragments.activity_home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_my_profile.MyProfileActivity;
import com.creative.share.apps.aqaar.activities_fragments.department.DepartmentActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_about.AboutActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_contact.ContactActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Chat;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Ads;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Map;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Map_Area;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Map_MyLocation;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.creative.share.apps.aqaar.activities_fragments.activity_search.SearchActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aqaar.activities_fragments.bank_activity.BanksActivity;
import com.creative.share.apps.aqaar.databinding.ActivityHomeBinding;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.CityDataModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.share.Common;
import com.creative.share.apps.aqaar.tags.Tags;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager manager;
    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle toggle;
    private Preferences preferences;
    private UserModel userModel;
    private boolean isNormal = false;
    private Fragment_Main_Map fragment_main_map;
    private Fragment_Main_Ads fragment_main_ads;
    private Fragment_Main_Map_MyLocation fragment_main_map_myLocation;
    private Fragment_Profile fragment_profile;
    private Fragment_Main_Map_Area fragment_main_map_area;
    private Fragment_Chat fragment_chat;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private boolean isCitySelected = false;
    public String city_id, lat, lng, category_id;
    private AHBottomNavigationItem item1;
    private CityDataModel.CityModel cityModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().getCurrentUser().delete();
                FirebaseAuth.getInstance().signOut();

            }
            DisplayFragmentArea();

        }
    }

    private void initView() {
        manager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open, R.string.close);
        toggle.syncState();
        setUpBottomNavigation();
        if (userModel != null) {
            updateuserimage();
        }
        binding.imageSearch.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        binding.llContact.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        });

        binding.llBanks.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, BanksActivity.class);
            startActivity(intent);
        });

        binding.llTerms.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, TermsActivity.class);
            startActivity(intent);
        });

        binding.llAbout.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });
        binding.imageUser.setOnClickListener(view ->
        {
            if(userModel!=null){
                Intent intent = new Intent(this, MyProfileActivity.class);
                startActivity(intent);}
            else {
                Common.CreateDialogAlert(HomeActivity.this, getString(R.string.please_sign_in_or_sign_up));

            }
        });
        binding.llLogout.setOnClickListener(view ->
        {
            if(userModel!=null){
            Logout();}
            else {
                Common.CreateDialogAlert(HomeActivity.this, getString(R.string.please_sign_in_or_sign_up));

            }
        });
        binding.llAddad.setOnClickListener(view ->
        {            if(userModel!=null){

            Intent intent = new Intent(this, DepartmentActivity.class);
            startActivity(intent);}
            else {
            Common.CreateDialogAlert(HomeActivity.this, getString(R.string.please_sign_in_or_sign_up));

        }
        });



    }

    private void updateuserimage()
    {

        Picasso.with(this).load(Tags.base_url + userModel.getUser().getUser_photo()).placeholder(R.drawable.ic_user_drawer).into(binding.imageUser);
binding.tvName.setText(userModel.getUser().getUser_name());
    }



    private void setUpBottomNavigation() {

        item1 = new AHBottomNavigationItem(getString(R.string.list), R.drawable.ic_list);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.areas), R.drawable.ic_area_with_pins);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.location), R.drawable.ic_gps);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.chat), R.drawable.ic_comment);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.profile), R.drawable.ic_user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.ahBottomNav.setTitleTextSizeInSp(14, 12);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.gray4));
        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);
        binding.ahBottomNav.addItem(item5);
        binding.ahBottomNav.setCurrentItem(0);

        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            userModel = preferences.getUserData(this);
            switch (position) {
                case 0:

                    if (isCitySelected) {
                        if (isNormal) {
                            isNormal = false;

                            item1.setTitle(getString(R.string.list));
                            item1.setDrawable(R.drawable.ic_list);
                            binding.ahBottomNav.refresh();
                            DisplayFragmentMainMap(cityModel);

                        } else {


                            item1.setTitle(getString(R.string.map));
                            item1.setDrawable(R.drawable.ic_map);
                            binding.ahBottomNav.refresh();

                            isNormal = true;
                            DisplayFragmentMainAds();


                        }
                    } else {
                        DisplayFragmentArea();
                    }

                    break;
                case 1:

                    DisplayFragmentArea();


                    break;
                case 2:
                    DisplayFragmentMyLocation();

                    break;
                case 3:
                    if (userModel != null) {
                        DisplayFragmentChat();
                    } else {
                        Common.CreateDialogAlert(HomeActivity.this, getString(R.string.please_sign_in_or_sign_up));
                    }
                    break;


                case 4:
                    if (userModel != null) {

                        DisplayFragmentProfile();

                    } else {
                        Common.CreateDialogAlert(HomeActivity.this, getString(R.string.please_sign_in_or_sign_up));
                    }


                    break;


            }
            return false;
        });

        binding.ahBottomNav.setCurrentItem(0, false);


    }


    public void DisplayFragmentMainMap(CityDataModel.CityModel cityModel) {
        this.cityModel = cityModel;
        isCitySelected = true;
        fragment_main_map = Fragment_Main_Map.newInstance(cityModel);

        if (fragment_profile != null && fragment_profile.isAdded()) {
            manager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_main_map_myLocation != null && fragment_main_map_myLocation.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_myLocation).commit();
        }

        if (fragment_main_map_area != null && fragment_main_map_area.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_area).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            manager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_main_ads != null && fragment_main_ads.isAdded()) {
            manager.beginTransaction().hide(fragment_main_ads).commit();
        }


        if (fragment_main_map.isAdded()) {
            manager.beginTransaction().show(fragment_main_map).commit();

        } else {
            manager.beginTransaction().add(R.id.fragment_home_container, fragment_main_map, "fragment_main_map").addToBackStack("fragment_main_map").commit();

        }
        binding.ahBottomNav.setCurrentItem(0, false);
        binding.tvTitle.setText(getString(R.string.home));

    }

    private void DisplayFragmentMainAds() {

        isCitySelected = true;

        fragment_main_ads = Fragment_Main_Ads.newInstance(city_id, lat, lng, category_id);

        if (fragment_profile != null && fragment_profile.isAdded()) {
            manager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_main_map_myLocation != null && fragment_main_map_myLocation.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_myLocation).commit();
        }

        if (fragment_main_map_area != null && fragment_main_map_area.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_area).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            manager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_main_map != null && fragment_main_map.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map).commit();
        }


        if (fragment_main_ads.isAdded()) {
            manager.beginTransaction().show(fragment_main_ads).commit();

        } else {
            manager.beginTransaction().add(R.id.fragment_home_container, fragment_main_ads, "fragment_main_ads").addToBackStack("fragment_main_ads").commit();

        }
        binding.ahBottomNav.setCurrentItem(0, false);
        binding.tvTitle.setText(getString(R.string.home));

    }

    private void DisplayFragmentArea() {

        isNormal = false;
        item1.setTitle(getString(R.string.list));
        item1.setDrawable(R.drawable.ic_list);
        binding.ahBottomNav.refresh();

        isCitySelected = false;
        fragment_main_map_area = Fragment_Main_Map_Area.newInstance();

        if (fragment_main_map != null && fragment_main_map.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            manager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_main_map_myLocation != null && fragment_main_map_myLocation.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_myLocation).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            manager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_main_ads != null && fragment_main_ads.isAdded()) {
            manager.beginTransaction().hide(fragment_main_ads).commit();
        }


        if (fragment_main_map_area.isAdded()) {
            manager.beginTransaction().show(fragment_main_map_area).commit();

        } else {
            manager.beginTransaction().add(R.id.fragment_home_container, fragment_main_map_area, "fragment_main_map_area").addToBackStack("fragment_main_map_area").commit();

        }
        binding.ahBottomNav.setCurrentItem(1, false);
        binding.tvTitle.setText(getString(R.string.areas));

    }

    private void DisplayFragmentMyLocation() {

        isNormal = false;

        item1.setTitle(getString(R.string.list));
        item1.setDrawable(R.drawable.ic_list);
        binding.ahBottomNav.refresh();
        isCitySelected = false;
        fragment_main_map_myLocation = Fragment_Main_Map_MyLocation.newInstance();

        if (fragment_main_map != null && fragment_main_map.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            manager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_main_map_area != null && fragment_main_map_area.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_area).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            manager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_main_ads != null && fragment_main_ads.isAdded()) {
            manager.beginTransaction().hide(fragment_main_ads).commit();
        }


        if (fragment_main_map_myLocation.isAdded()) {
            manager.beginTransaction().show(fragment_main_map_myLocation).commit();

        } else {
            manager.beginTransaction().add(R.id.fragment_home_container, fragment_main_map_myLocation, "fragment_main_map_myLocation").addToBackStack("fragment_main_map_myLocation").commit();

        }
        binding.ahBottomNav.setCurrentItem(2, false);
        binding.tvTitle.setText(getString(R.string.location));

    }

    private void DisplayFragmentChat() {

        isNormal = false;

        item1.setTitle(getString(R.string.list));
        item1.setDrawable(R.drawable.ic_list);
        binding.ahBottomNav.refresh();
        isCitySelected = false;
        if (fragment_chat == null) {
            fragment_chat = Fragment_Chat.newInstance();
        }

        if (fragment_main_map != null && fragment_main_map.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            manager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_main_map_myLocation != null && fragment_main_map_myLocation.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_myLocation).commit();
        }

        if (fragment_main_map_area != null && fragment_main_map_area.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_area).commit();
        }

        if (fragment_main_ads != null && fragment_main_ads.isAdded()) {
            manager.beginTransaction().hide(fragment_main_ads).commit();
        }


        if (fragment_chat.isAdded()) {
            manager.beginTransaction().show(fragment_chat).commit();

        } else {
            manager.beginTransaction().add(R.id.fragment_home_container, fragment_chat, "fragment_chat").addToBackStack("fragment_chat").commit();

        }
        binding.ahBottomNav.setCurrentItem(3, false);
        binding.tvTitle.setText(getString(R.string.chat));

    }


    private void DisplayFragmentProfile() {

        isNormal = false;

        item1.setTitle(getString(R.string.list));
        item1.setDrawable(R.drawable.ic_list);
        binding.ahBottomNav.refresh();
        isCitySelected = false;
        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }

        if (fragment_main_map != null && fragment_main_map.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map).commit();
        }
        if (fragment_main_map_myLocation != null && fragment_main_map_myLocation.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_myLocation).commit();
        }

        if (fragment_main_map_area != null && fragment_main_map_area.isAdded()) {
            manager.beginTransaction().hide(fragment_main_map_area).commit();
        }

        if (fragment_chat != null && fragment_chat.isAdded()) {
            manager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_main_ads != null && fragment_main_ads.isAdded()) {
            manager.beginTransaction().hide(fragment_main_ads).commit();
        }


        if (fragment_profile.isAdded()) {
            manager.beginTransaction().show(fragment_profile).commit();

        } else {
            manager.beginTransaction().add(R.id.fragment_home_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        binding.ahBottomNav.setCurrentItem(4, false);
        binding.tvTitle.setText(getString(R.string.profile));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = manager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = manager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (userModel == null) {
                navigateToSignInActivity();
            } else {
                finish();
            }
        }
    }

    private void navigateToSignInActivity() {
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    private void Logout() {
        preferences.create_update_userData(HomeActivity.this, null);
        preferences.createSession(HomeActivity.this, Tags.session_logout);
        navigateToSignInActivity();
    }


}
