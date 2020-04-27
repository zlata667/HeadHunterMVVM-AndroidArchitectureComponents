package com.example.headhunter.ui.startApp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Country;
import com.example.headhunter.data.model.ExperienceResponse;
import com.example.headhunter.ui.login.LoginActivity;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesFragment;
import com.example.headhunter.utils.ApiUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartSearchViewModel extends AndroidViewModel{

    private LinkedHashMap<String, String> regionsMap = new LinkedHashMap<>();
    private LinkedHashMap<String, String> experienceMap = new LinkedHashMap<>();
    private List<String> list = new ArrayList<>();
    private MutableLiveData<List<String>> regionList = new MutableLiveData<>();
    private MutableLiveData<String> searchText = new MutableLiveData<>();
    private MutableLiveData<String> autoCompleteText = new MutableLiveData<>();

    private MutableLiveData<Integer> errorVisibility = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private Disposable disposable;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        loadRegions();
        loadExperiences();
    };

    public StartSearchViewModel(@NonNull Application application){
        super(application);
        regionList.setValue(new ArrayList<>());
        loadRegions();
        loadExperiences();
    }

    private void loadExperiences(){
        disposable = ApiUtils.getApiService().getExperiences()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::saveExperience,
                        throwable -> {
                            Toast.makeText(getApplication().getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }

    private void saveExperience(ExperienceResponse experiences){
        for (ExperienceResponse.Experience experience : experiences.getExperience()) {
            experienceMap.put(experience.getName(), experience.getId());
        }
        saveMapInSharedPreferences(experienceMap, "ExperienceMap");
    }

    private void loadRegions(){
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
        for (Country country : countries) {
            for (Country.Region region : country.getAreas()) {
                list.add(region.getName());
                regionsMap.put(region.getName(), region.getId());
            }
        }
        saveMapInSharedPreferences(regionsMap, "RegionMap");
        regionList.postValue(list);
    }

    private void saveMapInSharedPreferences(LinkedHashMap<String, String> map, String name){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        if (sharedPreferences != null){
            JSONObject jsonObject = new JSONObject(map);
            String jsonString = jsonObject.toString();
            Editor editor = sharedPreferences.edit();
            editor.remove(name).apply();
            editor.putString(name, jsonString);
            editor.commit();
        }
    }

    public void openVacanciesFragment(Context context){
        Intent intent = new Intent(context, VacanciesActivity.class);
        Bundle args = new Bundle();

        if (regionsMap.containsKey(autoCompleteText.getValue())){
            args.putString(VacanciesFragment.SEARCH_REGION_NAME, autoCompleteText.getValue());
        }
        args.putString(VacanciesFragment.SEARCH_TEXT, searchText.getValue());
        args.putString(VacanciesFragment.SEARCH_EXPERIENCE_ID, null);
        intent.putExtra(VacanciesActivity.SEARCH_KEY, args);
        context.startActivity(intent);
    }

    public void openLoginFragment(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
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
