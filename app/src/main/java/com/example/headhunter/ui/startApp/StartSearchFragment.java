package com.example.headhunter.ui.startApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.data.model.Country;
import com.example.headhunter.databinding.StartSearchBinding;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesFragment;
import com.example.headhunter.utils.ApiUtils;
import com.example.headhunter.utils.factories.CustomSearchFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartSearchFragment extends Fragment {

    private StartSearchViewModel startSearchViewModel;

    static Fragment newInstance(){
        return new StartSearchFragment();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        CustomSearchFactory factory = new CustomSearchFactory(context);
        startSearchViewModel = ViewModelProviders.of(this, factory).get(StartSearchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        StartSearchBinding binding = StartSearchBinding.inflate(inflater, container, false);
        binding.setSearchModel(startSearchViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}
