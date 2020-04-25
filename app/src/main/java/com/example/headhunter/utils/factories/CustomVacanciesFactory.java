package com.example.headhunter.utils.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.headhunter.ui.vacancies.VacanciesAdapter;
import com.example.headhunter.ui.vacancies.VacanciesViewModel;

public class CustomVacanciesFactory extends ViewModelProvider.NewInstanceFactory{

    @NonNull
    private final Application mApplication;

    private VacanciesAdapter.OnItemClickListener mOnItemClickListener;
    private String mSearchText;
    private String mSearchRegionName;
    private String mSearchExperienceId;

    public CustomVacanciesFactory(@NonNull Application application, VacanciesAdapter.OnItemClickListener onItemClickListener,
                                  String searchText, String searchRegionName, String searchExperienceId){
        mApplication = application;
        mOnItemClickListener = onItemClickListener;
        mSearchText = searchText;
        mSearchRegionName = searchRegionName;
        mSearchExperienceId = searchExperienceId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new VacanciesViewModel(mApplication, mOnItemClickListener, mSearchText, mSearchRegionName, mSearchExperienceId);
    }
}
