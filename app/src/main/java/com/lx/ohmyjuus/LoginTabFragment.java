package com.lx.ohmyjuus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    EditText loginId;
    EditText loginPw;
    ImageButton goToSignup;
    Button loginButton;

    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        loginId = root.findViewById(R.id.ID);
        loginPw = root.findViewById(R.id.PW);
        goToSignup = root.findViewById(R.id.goToSignup);
        loginButton = root.findViewById(R.id.logInButton);

        loginId.setTranslationX(800);
        loginPw.setTranslationX(800);
        goToSignup.setTranslationX(800);
        loginButton.setTranslationX(800);

        loginId.setAlpha(v);
        loginPw.setAlpha(v);
        goToSignup.setAlpha(v);
        loginButton.setAlpha(v);

        loginId.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        loginPw.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        goToSignup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return root;
    }

}