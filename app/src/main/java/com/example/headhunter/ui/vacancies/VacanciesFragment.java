package com.example.headhunter.ui.vacancies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.headhunter.R;
import com.example.headhunter.databinding.VacanciesBinding;
import com.example.headhunter.ui.vacancy.VacancyActivity;
import com.example.headhunter.ui.vacancy.VacancyFragment;
import com.example.headhunter.utils.factories.CustomVacanciesFactory;

import java.util.Objects;

public class VacanciesFragment extends Fragment{

    public static final String SEARCH_TEXT = "SEARCH_TEXT";
    public static final String SEARCH_EXPERIENCE_ID = "SEARCH_EXPERIENCE_ID";
    public static final String SEARCH_REGION_NAME = "SEARCH_REGION_NAME";

    private String searchText;
    private String searchRegionName;
    private String searchExperienceId;

    private VacanciesViewModel vacanciesViewModel;
    private VacanciesAdapter.OnItemClickListener onItemClickListener = vacancyId -> {
        Intent intent = new Intent(getContext(), VacancyActivity.class);
        Bundle args = new Bundle();
        args.putString(VacancyFragment.VACANCY_ID, vacancyId);
        intent.putExtra(VacancyActivity.VACANCY_KEY, args);
        startActivity(intent);
    };

    static Fragment newInstance(Bundle args){
        VacanciesFragment fragment = new VacanciesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (getArguments() != null){
            searchText = getArguments().getString(SEARCH_TEXT);
            searchRegionName = getArguments().getString(SEARCH_REGION_NAME);
            searchExperienceId = getArguments().getString(SEARCH_EXPERIENCE_ID);
        }
        CustomVacanciesFactory factory = new CustomVacanciesFactory(getActivity().getApplication(),
                onItemClickListener, searchText, searchRegionName, searchExperienceId);
        vacanciesViewModel = ViewModelProviders.of(getActivity(), factory).get(VacanciesViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        VacanciesBinding binding = VacanciesBinding.inflate(inflater, container, false);
        binding.setVm(vacanciesViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        vacanciesViewModel.getGoToDialog().observe(getViewLifecycleOwner(), aBoolean -> {
            Bundle args = new Bundle();
            args.putString(SEARCH_TEXT, vacanciesViewModel.getSearchText().getValue());
            args.putString(SEARCH_EXPERIENCE_ID, vacanciesViewModel.getExperienceId().getValue());
            args.putString(SEARCH_REGION_NAME, vacanciesViewModel.getSearchRegionName().getValue());

            FilterDialogFragment dialogFragment = FilterDialogFragment.newInstance(args);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            dialogFragment.show(ft, "dialog_filter");
        });
        vacanciesViewModel.getSearchText().observe(getViewLifecycleOwner(), text ->{
            if (text != null && getActivity() != null){
                getActivity().setTitle(text);
            }
        });
    }
}
