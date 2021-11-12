package com.lx.ohmyjuus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {

    EditText inputId;
    EditText inputPw;
    EditText inputName;
    EditText inputNickname;
    EditText inputMobile;
    EditText inputBirth;
    Button signupButton;

    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        inputId = root.findViewById(R.id.inputId);
        inputPw = root.findViewById(R.id.inputPw);
        inputName = root.findViewById(R.id.inputName);
        inputNickname = root.findViewById(R.id.inputNick);
        inputMobile = root.findViewById(R.id.inputMobile);
        inputBirth = root.findViewById(R.id.inputBirth);
        signupButton = root.findViewById(R.id.signUpButton);

        inputId.setTranslationX(800);
        inputPw.setTranslationX(800);
        inputName.setTranslationX(800);
        inputNickname.setTranslationX(800);
        inputMobile.setTranslationX(800);
        inputBirth.setTranslationX(800);
        signupButton.setTranslationX(800);

        inputId.setAlpha(v);
        inputPw.setAlpha(v);
        inputName.setAlpha(v);
        inputNickname.setAlpha(v);
        inputMobile.setAlpha(v);
        inputBirth.setAlpha(v);
        signupButton.setAlpha(v);

        inputId.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        inputPw.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        inputName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        inputNickname.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        inputMobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        inputBirth.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1300).start();
        signupButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1500).start();

        return root;
    }

}
