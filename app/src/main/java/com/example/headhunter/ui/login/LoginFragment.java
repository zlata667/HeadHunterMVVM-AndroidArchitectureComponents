package com.example.headhunter.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.headhunter.databinding.LoginBinding;

public class LoginFragment extends Fragment{

    static Fragment newInstance(){
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        LoginBinding binding = LoginBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }
}
