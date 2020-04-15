package com.example.headhunter.ui.vacancy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.headhunter.R;
import com.example.headhunter.databinding.VacancyInfoBinding;
import com.example.headhunter.utils.factories.CustomVacancyFactory;

public class VacancyFragment extends Fragment{

    public static final String VACANCY_ID = "VACANCY_ID";
    private String vacancyId;
    private VacancyViewModel vacancyViewModel;

    static Fragment newInstance(Bundle args){
        VacancyFragment fragment = new VacancyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (getArguments() != null) {
            vacancyId = getArguments().getString(VACANCY_ID);
        }
        CustomVacancyFactory factory = new CustomVacancyFactory(vacancyId);
        vacancyViewModel = ViewModelProviders.of(this, factory).get(VacancyViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        VacancyInfoBinding binding = VacancyInfoBinding.inflate(inflater, container, false);
        binding.setVacancyModel(vacancyViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}
