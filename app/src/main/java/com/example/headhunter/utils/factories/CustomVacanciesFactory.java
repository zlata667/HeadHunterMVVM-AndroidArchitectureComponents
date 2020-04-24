package com.example.headhunter.utils.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.headhunter.ui.vacancies.VacanciesAdapter;
import com.example.headhunter.ui.vacancies.VacanciesViewModel;

public class CustomVacanciesFactory extends ViewModelProvider.NewInstanceFactory{

    private VacanciesAdapter.OnItemClickListener mOnItemClickListener;
    private String mSearchText;
    private String mSearchRegion;
    private String mSearchRegionName;
    private String mSearchExperienceId;

    public CustomVacanciesFactory(VacanciesAdapter.OnItemClickListener onItemClickListener,
                                  String searchText, String searchRegion, String searchRegionName, String searchExperienceId){
        mOnItemClickListener = onItemClickListener;
        mSearchText = searchText;
        mSearchRegion = searchRegion;
        mSearchRegionName = searchRegionName;
        mSearchExperienceId = searchExperienceId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new VacanciesViewModel(mOnItemClickListener, mSearchText, mSearchRegion, mSearchRegionName, mSearchExperienceId);
    }
}
