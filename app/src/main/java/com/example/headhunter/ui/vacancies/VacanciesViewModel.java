package com.example.headhunter.ui.vacancies;

import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacanciesViewModel extends ViewModel{

    private String mSearchText;
    private String mSearchRegion;

    private Disposable disposable;
    private VacanciesAdapter.OnItemClickListener mOnItemClickListener;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> loadVacancies(mSearchText, mSearchRegion);

    private MutableLiveData<Boolean> isErrorVisible = new MutableLiveData<>();
    private MutableLiveData<List<Vacancies.ItemsBean>> mVacancies = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    public VacanciesViewModel(VacanciesAdapter.OnItemClickListener onItemClickListener,
                              String searchText, String searchRegion){
        mOnItemClickListener = onItemClickListener;
        mSearchText = searchText;
        mSearchRegion = searchRegion;

        mVacancies.setValue(new ArrayList<>());
        loadVacancies(searchText, searchRegion);
    }

    void loadVacancies(String searchText, String searchRegion){
        disposable = ApiUtils.getApiService().getVacancies(searchText, searchRegion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.postValue(true))
                .doFinally(() -> isLoading.postValue(false))
                .subscribe(
                        vacancies -> {
                            isErrorVisible.postValue(false);
                            mVacancies.postValue(vacancies.getItems());
                        },
                        throwable -> isErrorVisible.postValue(true)
                );
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

    public MutableLiveData<Boolean> getIsErrorVisible(){
        return isErrorVisible;
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
}
