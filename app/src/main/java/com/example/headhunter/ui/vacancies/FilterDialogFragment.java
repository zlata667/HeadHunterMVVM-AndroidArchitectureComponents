package com.example.headhunter.ui.vacancies;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.headhunter.R;
import com.example.headhunter.databinding.FilterSearchBinding;
import com.example.headhunter.utils.factories.CustomVacanciesFactory;

import java.util.Objects;

public class FilterDialogFragment extends DialogFragment{

    private VacanciesViewModel viewModel;
    private String searchText;
    private String regionName;
    private String experienceId;

    static FilterDialogFragment newInstance(Bundle bundle){
        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        assert window != null;
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FilterSearchBinding binding = FilterSearchBinding.inflate(inflater, container, false);
        binding.setFilterVm(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (getArguments() != null){
            searchText = getArguments().getString(VacanciesFragment.SEARCH_TEXT);
            regionName = getArguments().getString(VacanciesFragment.SEARCH_REGION_NAME);
            experienceId = getArguments().getString(VacanciesFragment.SEARCH_EXPERIENCE_ID);
        }

        CustomVacanciesFactory factory = new CustomVacanciesFactory(getActivity().getApplication(),
                null, searchText, regionName, experienceId);
        viewModel = ViewModelProviders.of(getActivity(), factory).get(VacanciesViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        viewModel.getCloseDialogAndRefresh().observe(getViewLifecycleOwner(), aBoolean -> {
            viewModel.loadVacancies(viewModel.getSearchText().getValue(),
                    viewModel.getSearchRegionName().getValue(), viewModel.getExperienceId().getValue());
            dismiss();
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
