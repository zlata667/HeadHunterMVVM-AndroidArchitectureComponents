package com.example.headhunter.ui.vacancies;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.utils.ApiUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Checksum;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacanciesViewModel extends ViewModel{

    private MutableLiveData<String> mSearchText = new MutableLiveData<>();
    private MutableLiveData<String> mSearchRegion = new MutableLiveData<>();
    private MutableLiveData<String> mSearchRegionName = new MutableLiveData<>();
    private MutableLiveData<String> mExperienceId = new MutableLiveData<>();

    private Disposable disposable;
    private VacanciesAdapter.OnItemClickListener mOnItemClickListener;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            () -> loadVacancies(mSearchText.getValue(), mSearchRegion.getValue(), mExperienceId.getValue());

    private MutableLiveData<Integer> isErrorVisible = new MutableLiveData<>();
    private MutableLiveData<Integer> isRecyclerVisible = new MutableLiveData<>();
    private MutableLiveData<List<Vacancies.ItemsBean>> mVacancies = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();



    public VacanciesViewModel(VacanciesAdapter.OnItemClickListener onItemClickListener,
                              String searchText, String searchRegion, String searchRegionName, String searchExperienceId){
        mOnItemClickListener = onItemClickListener;
        mSearchText.setValue(searchText);
        mSearchRegion.setValue(searchRegion);
        mSearchRegionName.setValue(searchRegionName);
        mExperienceId.setValue(searchExperienceId);


        mVacancies.setValue(new ArrayList<>());
        loadVacancies(mSearchText.getValue(), mSearchRegion.getValue(), mExperienceId.getValue());
    }

    public void loadVacancies(String searchText, String searchRegion, String searchExperience){
        disposable = ApiUtils.getApiService().getVacancies(searchText, searchRegion, searchExperience)
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

    public void onButtonFilterClick(Context context){
        Bundle args = new Bundle();
        args.putString(FilterDialogFragment.SEARCH_TEXT, mSearchText.getValue());
        args.putString(FilterDialogFragment.REGION_NAME, mSearchRegionName.getValue());
        args.putString(FilterDialogFragment.EXPERIENCE_ID, mExperienceId.getValue());

        FilterDialogFragment dialogFragment = FilterDialogFragment.newInstance(args);

        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
        dialogFragment.show(manager, "filter");

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

}
