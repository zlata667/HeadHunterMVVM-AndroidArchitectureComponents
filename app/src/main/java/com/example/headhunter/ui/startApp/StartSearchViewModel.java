package com.example.headhunter.ui.startApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Country;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesFragment;
import com.example.headhunter.utils.ApiUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartSearchViewModel{

    private Disposable disposable;
    private Context mContext;

    private ObservableBoolean isErrorVisible = new ObservableBoolean(false);
    private ObservableBoolean isLoading = new ObservableBoolean(false);
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> loadRegions();

    private ObservableArrayList<String> regionList = new ObservableArrayList<>();
    private Map<String, String> regionMap = new HashMap<>();
    private String searchText = "";
    private String autoCompleteText = "";

    public StartSearchViewModel(Context context){
        mContext = context;
    }

    public void loadRegions(){
        disposable = ApiUtils.getApiService().getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doFinally(() -> isLoading.set(false))
                .subscribe(
                        countries -> {
                            isErrorVisible.set(false);
                            bind(countries);
                        },
                        throwable -> isErrorVisible.set(true)
                );
    }

    private void bind(List<Country> countries){
        for (Country country : countries){
            for (Country.Region region : country.getAreas()){
                regionMap.put(region.getName(), region.getId());
            }
        }
        regionList.addAll(regionMap.keySet());
    }

    public void openVacanciesFragment(){
        Intent intent = new Intent(mContext, VacanciesActivity.class);
        Bundle args = new Bundle();

        if (regionMap.containsKey(autoCompleteText)){
            args.putString(VacanciesFragment.SEARCH_REGION, regionMap.get(autoCompleteText));
        }

        args.putString(VacanciesFragment.SEARCH_TEXT, searchText);
        intent.putExtra(VacanciesActivity.SEARCH_KEY, args);
        mContext.startActivity(intent);
    }


    void dispatchDetach(){
        if (disposable != null){
            disposable.dispose();
        }
    }

    public ObservableBoolean getIsErrorVisible(){
        return isErrorVisible;
    }

    public ObservableBoolean getIsLoading(){
        return isLoading;
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener(){
        return onRefreshListener;
    }

    public ObservableArrayList<String> getRegionList(){
        return regionList;
    }

    public String getSearchText(){
        return searchText;
    }

    public Context getView(){
        return mContext;
    }

    public String getAutoCompleteText(){
        return autoCompleteText;
    }

    public void setSearchText(String searchText){
        this.searchText = searchText;
    }

    public void setAutoCompleteText(String autoCompleteText){
        this.autoCompleteText = autoCompleteText;
    }
}
