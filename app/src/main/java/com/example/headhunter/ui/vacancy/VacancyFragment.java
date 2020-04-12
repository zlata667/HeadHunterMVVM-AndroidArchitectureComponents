package com.example.headhunter.ui.vacancy;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.data.model.Vacancy;
import com.example.headhunter.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacancyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String VACANCY_ID = "VACANCY_ID";

    private Disposable disposable;
    private NestedScrollView vacancyView;
    private View errorView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView vacancyName;
    private TextView employerName;
    private TextView salary;
    private TextView vacancyDescription;

    private String vacancyId;

    static Fragment newInstance(Bundle args){
        VacancyFragment fragment = new VacancyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fr_vacancy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        vacancyView = view.findViewById(R.id.view_vacancy);
        errorView = view.findViewById(R.id.errorView);

        vacancyName = view.findViewById(R.id.vacancy_name);
        employerName = view.findViewById(R.id.employer_name);
        salary = view.findViewById(R.id.salary);
        vacancyDescription = view.findViewById(R.id.vacancy_description);
        swipeRefreshLayout = view.findViewById(R.id.refresher);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null){
            vacancyId = getArguments().getString(VACANCY_ID);
        }

        if (getActivity() != null){
            getActivity().setTitle("Vacancy");
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        vacancyView.setVisibility(View.VISIBLE);

        onRefresh();
    }

    private void getVacancy(){
        disposable = ApiUtils.getApiService().getVacancy(vacancyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> swipeRefreshLayout.setRefreshing(true))
                .doFinally(() -> swipeRefreshLayout.setRefreshing(false))
                .subscribe(vacancy -> {
                            errorView.setVisibility(View.GONE);
                            vacancyView.setVisibility(View.VISIBLE);
                            bind(vacancy);
                        },
                        throwable -> {
                            errorView.setVisibility(View.VISIBLE);
                            vacancyView.setVisibility(View.GONE);
                        });
    }

    private void bind(Vacancy vacancy){
        vacancyName.setText(vacancy.getName());
        employerName.setText(vacancy.getEmployer().getName());
        if (vacancy.getSalary() != null){
            salary.setText(String.valueOf(vacancy.getSalary().getFrom()).concat(" ").concat(vacancy.getSalary().getCurrency()));
        }
        vacancyDescription.setText(Html.fromHtml(vacancy.getDescription()));
    }

    @Override
    public void onDetach(){
        if (disposable!= null) {
            disposable.dispose();
        }
        super.onDetach();
    }

    @Override
    public void onRefresh(){
        getVacancy();
    }
}
