package com.example.headhunter.ui.vacancies;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.common.SingleLiveEvent;
import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.utils.ApiUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacanciesViewModel extends AndroidViewModel{

    private MutableLiveData<String> mSearchText = new MutableLiveData<>();
    private MutableLiveData<String> mSearchRegionId = new MutableLiveData<>();
    private MutableLiveData<String> mSearchRegionName = new MutableLiveData<>();
    private MutableLiveData<String> mExperienceId = new MutableLiveData<>();
    private MutableLiveData<List<String>> regionList = new MutableLiveData<>();

    private SingleLiveEvent<Boolean> toDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> closeDialog = new SingleLiveEvent<>();

    private MutableLiveData<Integer> isErrorVisible = new MutableLiveData<>();
    private MutableLiveData<Integer> isRecyclerVisible = new MutableLiveData<>();
    private MutableLiveData<List<Vacancies.ItemsBean>> mVacancies = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Integer> checkedId = new MutableLiveData<>();

    private LinkedHashMap<String, String> experienceMap;
    private LinkedHashMap<String, String> regionMap;

    private Disposable disposable;
    private VacanciesAdapter.OnItemClickListener mOnItemClickListener;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            () -> loadVacancies(mSearchText.getValue(), mSearchRegionName.getValue(), mExperienceId.getValue());

    public VacanciesViewModel(@NonNull Application application, VacanciesAdapter.OnItemClickListener onItemClickListener,
                              String searchText, String searchRegionName, String searchExperienceId){
        super(application);
        mVacancies.setValue(new ArrayList<>());
        mOnItemClickListener = onItemClickListener;

        experienceMap = getMapFromSharedPreferences("ExperienceMap");
        regionMap = getMapFromSharedPreferences("RegionMap");
        regionList.setValue(new ArrayList<String>(regionMap.keySet()));
        loadVacancies(searchText, searchRegionName, searchExperienceId);

        mSearchRegionId.setValue(regionMap.get(searchRegionName));
        mSearchRegionName.setValue(searchRegionName);
        checkedId.setValue(Math.abs("Показать все".hashCode()));
    }

    void loadVacancies(String searchText, String searchRegionName, String searchExperience){
       if (searchExperience != null){
           for (Entry<String, String> entry : experienceMap.entrySet()){
               if (entry.getValue() != null && entry.getValue().equals(searchExperience)){
                   checkedId.setValue(Math.abs(entry.getKey().hashCode()));
               }
           }
       }
        mSearchText.setValue(searchText);
        mExperienceId.setValue(searchExperience);
        mSearchRegionId.setValue(regionMap.get(searchRegionName));
        mSearchRegionName.setValue(searchRegionName);

        disposable = ApiUtils.getApiService().getVacancies(searchText, mSearchRegionId.getValue(), searchExperience)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.postValue(true))
                .doFinally(() -> isLoading.postValue(false))
                .subscribe(
                        vacancies -> {
                            isErrorVisible.postValue(View.GONE);
                            isRecyclerVisible.postValue(View.VISIBLE);
                            mVacancies.postValue(vacancies.getItems());
                        },
                        throwable ->{
                            isErrorVisible.postValue(View.VISIBLE);
                            isRecyclerVisible.postValue(View.GONE);
                        }
                );
    }

    public void onExperienceChanged(RadioGroup radioGroup, int id){
        RadioButton selectedButton = radioGroup.findViewById(id);
        mExperienceId.postValue(experienceMap.get(selectedButton.getText()));
    }

    public void openDialogFragment(){
        toDialog.call();
    }

    public void closeDialogFragment(){
        closeDialog.call();
    }

    private LinkedHashMap<String, String> getMapFromSharedPreferences(String name){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        try{
            if (sharedPreferences != null){
                String jsonString = sharedPreferences.getString(name, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    map.put(key, value);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public void onCleared(){
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public VacanciesAdapter.OnItemClickListener getOnItemClickListener(){
        return mOnItemClickListener;
    }

    public MutableLiveData<Integer> getIsErrorVisible(){
        return isErrorVisible;
    }

    public MutableLiveData<Integer> getIsRecyclerVisible(){
        return isRecyclerVisible;
    }

    public MutableLiveData<List<Vacancies.ItemsBean>> getVacancies(){
        return mVacancies;
    }

    public MutableLiveData<Boolean> getIsLoading(){
        return isLoading;
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener(){
        return onRefreshListener;
    }

    public MutableLiveData<String> getSearchText(){
        return mSearchText;
    }

    public MutableLiveData<String> getSearchRegionName(){
        return mSearchRegionName;
    }

    public MutableLiveData<Integer> getCheckedId(){
        return checkedId;
    }

    public void setCheckedId(MutableLiveData<Integer> checkedId){
        this.checkedId = checkedId;
    }

    MutableLiveData<String> getExperienceId(){
        return mExperienceId;
    }

    MutableLiveData<Boolean> getGoToDialog(){
        return toDialog;
    }

    MutableLiveData<Boolean> getCloseDialogAndRefresh(){
        return closeDialog;
    }

    public MutableLiveData<List<String>> getRegionList(){
        return regionList;
    }

    public Map<String, String> getExperienceMap(){
        return experienceMap;
    }
}
