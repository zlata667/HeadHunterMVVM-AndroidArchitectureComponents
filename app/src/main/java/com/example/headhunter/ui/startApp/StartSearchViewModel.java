package com.example.headhunter.ui.startApp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.data.model.Country;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesFragment;
import com.example.headhunter.utils.ApiUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartSearchViewModel extends AndroidViewModel{

    private Disposable disposable;
    private MutableLiveData<String> searchText = new MutableLiveData<>();
    private MutableLiveData<String> autoCompleteText = new MutableLiveData<>();
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> loadRegions();

    private MutableLiveData<Integer> errorVisibility = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private Map<String, String> regionsMap = new HashMap<>();
    private List<String> list = new ArrayList<>();
    private MutableLiveData<List<String>> regionList = new MutableLiveData<>();

    public StartSearchViewModel(@NonNull Application application){
        super(application);
        regionList.setValue(new ArrayList<>());
        loadRegions();
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
        for (Country country : countries){
            for (Country.Region region : country.getAreas()){
                list.add(region.getName());
                regionsMap.put(region.getName(), region.getId());
            }
        }
        saveRegionMap(regionsMap);
        regionList.postValue(list);
    }

    private void saveRegionMap(Map<String, String> regionsMap){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("RegionsMap", Context.MODE_PRIVATE);
        if (sharedPreferences != null){
            JSONObject jsonObject = new JSONObject(regionsMap);
            String jsonString = jsonObject.toString();
            Editor editor = sharedPreferences.edit();
            editor.remove("Map").apply();
            editor.putString("Map", jsonString);
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
