package com.example.headhunter.ui.vacancies;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.headhunter.R;
import com.example.headhunter.common.SingleFragmentActivity;
import com.example.headhunter.ui.startApp.StartSearchFragment;
import com.example.headhunter.ui.vacancy.VacancyFragment;

public class VacanciesActivity extends SingleFragmentActivity{

    public static final String SEARCH_KEY = "SEARCH_KEY";

    @Override
    protected Fragment getFragment(){
        if (getIntent() != null){
            return VacanciesFragment.newInstance(getIntent().getBundleExtra(SEARCH_KEY));
        }
        throw new IllegalStateException("getIntent cannot be null");
    }
}
