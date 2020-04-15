package com.example.headhunter.utils.factories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.headhunter.ui.startApp.StartSearchViewModel;

public class CustomSearchFactory extends ViewModelProvider.NewInstanceFactory{

    private Context mContext;

    public CustomSearchFactory(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new StartSearchViewModel(mContext);
    }
}
