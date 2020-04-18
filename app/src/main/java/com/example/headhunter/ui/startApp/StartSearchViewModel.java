package com.example.headhunter.ui.startApp;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Country;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesFragment;
import com.example.headhunter.utils.ApiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartSearchViewModel extends ViewModel{

    private Disposable disposable;
    private Context mContext;
    private MutableLiveData<String> searchText = new MutableLiveData<>();
    private MutableLiveData<String> autoCompleteText = new MutableLiveData<>();
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> loadRegions();

    private MutableLiveData<Integer> errorVisibility = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private Map<String, String> regionsMap = new HashMap<>();
    private List<String> list = new ArrayList<>();
    private MutableLiveData<List<String>> regionList = new MutableLiveData<>();

    public StartSearchViewModel(Context context){
        mContext = context;
        regionList.setValue(new ArrayList<>());
        loadRegions();
    }

    public void loadRegions(){
        disposable = ApiUtils.getApiService().getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.postValue(true))
                .doFinally(() -> isLoading.postValue(false))
                .subscribe(
                        countries -> {
                            errorVisibility.postValue(View.GONE);
                            bind(countries);
                        },
                        throwable -> errorVisibility.postValue(View.VISIBLE)
                );
    }

    private void bind(List<Country> countries){
        for (Country country : countries){
            for (Country.Region region : country.getAreas()){
                list.add(region.getName());
                regionsMap.put(region.getName(), region.getId());
            }
        }
        regionList.postValue(list);
    }

    public void openVacanciesFragment(){
        Intent intent = new Intent(mContext, VacanciesActivity.class);
        Bundle args = new Bundle();
        args.putString(VacanciesFragment.SEARCH_REGION, regionsMap.get(autoCompleteText.getValue()));
        args.putString(VacanciesFragment.SEARCH_TEXT, searchText.getValue());
        intent.putExtra(VacanciesActivity.SEARCH_KEY, args);
        mContext.startActivity(intent);
    }


    @Override
    public void onCleared(){
        if (disposable != null){
            disposable.dispose();
        }
    }

    public MutableLiveData<Integer> getErrorVisibility(){
        return errorVisibility;
    }

    public MutableLiveData<Boolean> getIsLoading(){
        return isLoading;
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener(){
        return onRefreshListener;
    }

    public MutableLiveData<String> getSearchText(){
        return searchText;
    }

    public Context getView(){
        return mContext;
    }

    public MutableLiveData<String> getAutoCompleteText(){
        return autoCompleteText;
    }

    public MutableLiveData<List<String>> getRegionList(){
        return regionList;
    }

    public void setRegionList(MutableLiveData<List<String>> regionList){
        this.regionList = regionList;
    }

    public void setSearchText(MutableLiveData<String> searchText){
        this.searchText = searchText;
    }

    public void setAutoCompleteText(MutableLiveData<String> autoCompleteText){
        this.autoCompleteText = autoCompleteText;
    }
}
