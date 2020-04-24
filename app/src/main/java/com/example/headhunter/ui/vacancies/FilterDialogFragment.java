package com.example.headhunter.ui.vacancies;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.headhunter.R;
import com.example.headhunter.databinding.FilterSearchBinding;
import com.example.headhunter.utils.factories.CustomFilterDialogFactory;

import java.util.Objects;

import static androidx.appcompat.app.AlertDialog.*;

public class FilterDialogFragment extends DialogFragment{

    static final String SEARCH_TEXT = "SEARCH_TEXT";
    static final String REGION_NAME = "REGION_NAME";
    static final String EXPERIENCE_ID = "EXPERIENCE_ID";

    private FilterDialogViewModel viewModel;
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
            searchText = getArguments().getString(SEARCH_TEXT);
            regionName = getArguments().getString(REGION_NAME);
            experienceId = getArguments().getString(EXPERIENCE_ID);
        }

        CustomFilterDialogFactory factory = new CustomFilterDialogFactory(searchText, regionName, experienceId);
        viewModel = ViewModelProviders.of(this, factory).get(FilterDialogViewModel.class);
    }

    @Override
    public void onPause(){
        super.onPause();
        dismiss();
    }
}
