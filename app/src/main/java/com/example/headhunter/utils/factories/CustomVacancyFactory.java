package com.example.headhunter.utils.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.headhunter.ui.vacancy.VacancyViewModel;

public class CustomVacancyFactory extends ViewModelProvider.NewInstanceFactory{

    private String mVacancyId;

    public CustomVacancyFactory(String vacancyId){
        mVacancyId = vacancyId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new VacancyViewModel(mVacancyId);
    }
}
