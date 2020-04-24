package com.example.headhunter.utils.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.headhunter.ui.vacancies.FilterDialogViewModel;
import com.example.headhunter.ui.vacancy.VacancyViewModel;

public class CustomFilterDialogFactory extends ViewModelProvider.NewInstanceFactory{

    private String mSearchText;
    private String mRegionName;
    private String mExperienceId;

    public CustomFilterDialogFactory(String searchText, String regionName, String experienceId){
        mSearchText = searchText;
        mRegionName = regionName;
        mExperienceId = experienceId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new FilterDialogViewModel(mSearchText, mRegionName, mExperienceId);
    }
}
