package com.example.headhunter.ui.vacancies;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.common.SingleLiveEvent;
import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.utils.ApiUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Checksum;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
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

    private Map<String, Integer> experienceMap = new HashMap<>();
    private Map<String, String> regionMap;

    private Disposable disposable;
    private VacanciesAdapter.OnItemClickListener mOnItemClickListener;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            () -> loadVacancies(mSearchText.getValue(), mSearchRegionName.getValue(), mExperienceId.getValue());

    public VacanciesViewModel(@NonNull Application application, VacanciesAdapter.OnItemClickListener onItemClickListener,
                              String searchText, String searchRegionName, String searchExperienceId){
        super(application);
        mVacancies.setValue(new ArrayList<>());
        mOnItemClickListener = onItemClickListener;

        regionMap = getRegionMap();
        regionList.setValue(new ArrayList<String>(regionMap.keySet()));
        loadVacancies(searchText, searchRegionName, searchExperienceId);

        mSearchRegionId.setValue(regionMap.get(searchRegionName));
        mSearchRegionName.setValue(searchRegionName);

        experienceMap.put(null, R.id.rb_exp_all);
        experienceMap.put("noExperience", R.id.rb_exp_0);
        experienceMap.put("between1And3", R.id.rb_exp_1_3);
        experienceMap.put("between3And6", R.id.rb_exp_3_6);
        experienceMap.put("moreThan6", R.id.rb_exp_6);
        checkedId.setValue(experienceMap.get(searchExperienceId));
    }

    void loadVacancies(String searchText, String searchRegionName, String searchExperience){
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
        for (Entry<String, Integer> entry : experienceMap.entrySet()){
            if (entry.getValue().equals(id)){
                mExperienceId.postValue(entry.getKey());
            }
        }
    }

    public void openDialogFragment(){
        toDialog.call();
    }

    public void closeDialogFragment(){
        closeDialog.call();
    }

    private Map<String, String> getRegionMap(){
        Map<String, String> regionMap = new HashMap<>();
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("RegionsMap", Context.MODE_PRIVATE);
        try{
            if (sharedPreferences != null){
                String jsonString = sharedPreferences.getString("Map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    regionMap.put(key, value);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return regionMap;
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
}
