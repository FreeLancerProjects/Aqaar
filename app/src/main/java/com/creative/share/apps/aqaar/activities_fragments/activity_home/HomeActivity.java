package com.creative.share.apps.aqaar.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Chat;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Ads;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Map;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Map_Area;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Main_Map_MyLocation;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.creative.share.apps.aqaar.activities_fragments.activity_search.SearchActivity;
import com.creative.share.apps.aqaar.databinding.ActivityHomeBinding;
import com.creative.share.apps.aqaar.language.LanguageHelper;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.share.Common;

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
            DisplayFragmentMainMap();
        }
    }

    private void initView() {
        manager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open, R.string.close);
        toggle.syncState();
        setUpBottomNavigation();

        binding.imageSearch.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });


    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.list), R.drawable.ic_list);
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
                    if (isNormal) {
                        isNormal = false;

                        item1.setTitle(getString(R.string.list));
                        item1.setDrawable(R.drawable.ic_list);
                        binding.ahBottomNav.refresh();
                        DisplayFragmentMainMap();

                    } else {


                        item1.setTitle(getString(R.string.map));
                        item1.setDrawable(R.drawable.ic_map);
                        binding.ahBottomNav.refresh();

                        isNormal = true;
                        DisplayFragmentMainAds();


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


    private void DisplayFragmentMainMap() {

        fragment_main_map = Fragment_Main_Map.newInstance();

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

        fragment_main_ads = Fragment_Main_Ads.newInstance();

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
            manager.beginTransaction().hide(fragment_chat).commit();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = manager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = manager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            if (fragment_main_map != null && fragment_main_map.isAdded() && fragment_main_map.isVisible()) {
                if (userModel == null) {
                    navigateToSignInActivity();
                } else {
                    finish();
                }
            } else if (fragment_main_ads != null && fragment_main_ads.isAdded() && fragment_main_ads.isVisible()) {
                if (userModel == null) {
                    navigateToSignInActivity();
                } else {
                    finish();
                }
            } else {
                if (isNormal) {
                    DisplayFragmentMainMap();
                } else {
                    DisplayFragmentMainAds();
                }
            }
        }
    }

    private void navigateToSignInActivity() {
        finish();
    }
}
