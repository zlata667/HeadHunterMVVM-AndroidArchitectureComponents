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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.data.model.Country;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesFragment;
import com.example.headhunter.utils.ApiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartSearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Disposable disposable;
    private Map<String, String> regionMap = new HashMap<>();
    private List<String> regionNamesList;

    private View searchView;
    private View errorView;

    private AutoCompleteTextView autoCompleteTextView;
    private EditText editTextSearch;
    private SwipeRefreshLayout swipeRefreshLayout;

    static Fragment newInstance(){
        return new StartSearchFragment();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fr_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        searchView = view.findViewById(R.id.view_search);
        errorView = view.findViewById(R.id.errorView);
        swipeRefreshLayout = view.findViewById(R.id.refresher);
        
        editTextSearch = view.findViewById(R.id.et_search);
        view.findViewById(R.id.btn_search).setOnClickListener(v -> {
            onButtonClick();
        });
        autoCompleteTextView = view.findViewById(R.id.et_city);
    }

    private void onButtonClick(){
        Intent intent = new Intent(getActivity(), VacanciesActivity.class);
        Bundle args = new Bundle();

        String region = autoCompleteTextView.getText().toString();
        if (regionMap.containsKey(region)){
            args.putString(VacanciesFragment.SEARCH_REGION, regionMap.get(region));
        }

        args.putString(VacanciesFragment.SEARCH_TEXT, editTextSearch.getText().toString());
        intent.putExtra(VacanciesActivity.SEARCH_KEY, args);
        startActivity(intent);
    }

    private void getCities(){
        disposable = ApiUtils.getApiService().getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> swipeRefreshLayout.setRefreshing(true))
                .doFinally(() -> swipeRefreshLayout.setRefreshing(false))
                .subscribe(
                        countries -> {
                            errorView.setVisibility(View.GONE);
                            searchView.setVisibility(View.VISIBLE);
                            bind(countries);
                        },
                        throwable -> Toast.makeText(getContext(), "Не удалось загрузить список регионов", Toast.LENGTH_SHORT).show()
                );
    }

    private void bind(List<Country> countries){
        for (Country country : countries){
            for (Country.Region region : country.getAreas()){
                regionMap.put(region.getName(), region.getId());
            }
        }

        regionNamesList = new ArrayList<>(regionMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, regionNamesList);
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        searchView.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
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
        getCities();
    }
}
