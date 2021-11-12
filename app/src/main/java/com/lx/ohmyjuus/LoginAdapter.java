package com.lx.ohmyjuus;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter {

    private Context context;

    public LoginAdapter(Context context) {
        this.context = context;

    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;

            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;

            default:
                return null;
        }
    }
}