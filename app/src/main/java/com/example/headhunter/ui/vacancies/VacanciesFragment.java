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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;

import com.example.headhunter.databinding.VacanciesBinding;
import com.example.headhunter.ui.vacancy.VacancyActivity;
import com.example.headhunter.ui.vacancy.VacancyFragment;
import com.example.headhunter.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacanciesFragment extends Fragment{

    public static final String SEARCH_TEXT = "SEARCH_TEXT";
    public static final String SEARCH_REGION = "SEARCH_REGION";
    private String searchText;
    private String searchRegion;

    private VacanciesViewModel vacanciesViewModel;
    private VacanciesAdapter.OnItemClickListener onItemClickListener = vacancyId -> {
        Intent intent = new Intent(getActivity(), VacancyActivity.class);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        vacanciesViewModel = new VacanciesViewModel(onItemClickListener, searchText, searchRegion);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        VacanciesBinding binding = VacanciesBinding.inflate(inflater, container, false);
        binding.setVm(vacanciesViewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) getActivity().setTitle("Vacancies");

        if (getArguments() != null){
            searchText = getArguments().getString(SEARCH_TEXT);
            searchRegion = getArguments().getString(SEARCH_REGION);
        }
        vacanciesViewModel.loadVacancies(searchText, searchRegion);
    }

    @Override
    public void onDetach() {
        vacanciesViewModel.dispatchDetach();
        super.onDetach();
    }
}
