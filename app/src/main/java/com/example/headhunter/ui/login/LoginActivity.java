package com.example.headhunter.ui.login;

import androidx.fragment.app.Fragment;

import com.example.headhunter.common.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity{
    @Override
    protected Fragment getFragment(){
        return LoginFragment.newInstance();
    }
}
