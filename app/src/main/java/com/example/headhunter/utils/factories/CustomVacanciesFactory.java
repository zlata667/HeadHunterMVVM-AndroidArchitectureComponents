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

    public CustomVacanciesFactory(VacanciesAdapter.OnItemClickListener onItemClickListener, String searchText, String searchRegion){
        mOnItemClickListener = onItemClickListener;
        mSearchText = searchText;
        mSearchRegion = searchRegion;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new VacanciesViewModel(mOnItemClickListener, mSearchText, mSearchRegion);
    }
}
