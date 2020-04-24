package com.example.headhunter.ui.startApp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.headhunter.databinding.StartSearchBinding;

public class StartSearchFragment extends Fragment {

    private StartSearchViewModel startSearchViewModel;

    static Fragment newInstance(){
        return new StartSearchFragment();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        NewInstanceFactory factory = new NewInstanceFactory();
        startSearchViewModel = ViewModelProviders.of(this, factory).get(StartSearchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        StartSearchBinding binding = StartSearchBinding.inflate(inflater, container, false);
        binding.setSearchModel(startSearchViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}
