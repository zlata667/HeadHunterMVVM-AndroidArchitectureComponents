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

import com.example.headhunter.databinding.VacancyInfoBinding;

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
        vacancyViewModel = new VacancyViewModel(vacancyId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        VacancyInfoBinding binding = VacancyInfoBinding.inflate(inflater, container, false);
        binding.setVacancyModel(vacancyViewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) vacancyId = getArguments().getString(VACANCY_ID);
        if (getActivity() != null) getActivity().setTitle("Vacancy");
        vacancyViewModel.loadVacancy(vacancyId);
    }

    @Override
    public void onDetach(){
        vacancyViewModel.dispatchDetach();
        super.onDetach();
    }
}
