package com.example.headhunter.ui.vacancies;

import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacanciesViewModel{

    private Disposable disposable;
    private VacanciesAdapter.OnItemClickListener mOnItemClickListener;

    private String mSearchText;
    private String mSearchRegion;

    private ObservableBoolean isErrorVisible = new ObservableBoolean(false);
    private ObservableArrayList<Vacancies.ItemsBean> mVacancies = new ObservableArrayList<>();
    private ObservableBoolean isLoading = new ObservableBoolean(false);
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> loadVacancies(mSearchText, mSearchRegion);


    VacanciesViewModel(VacanciesAdapter.OnItemClickListener onItemClickListener,
                       String searchText, String searchRegion){
        mOnItemClickListener = onItemClickListener;
        mSearchText = searchText;
        mSearchRegion = searchRegion;
    }

    void loadVacancies(String searchText, String searchRegion){
        disposable = ApiUtils.getApiService().getVacancies(searchText, searchRegion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doFinally(() -> isLoading.set(false))
                .subscribe(
                        vacancies -> {
                            isErrorVisible.set(false);
                            mVacancies.addAll(vacancies.getItems());
                        },
                        throwable -> isErrorVisible.set(true)
                );
    }

    void dispatchDetach(){
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public VacanciesAdapter.OnItemClickListener getOnItemClickListener(){
        return mOnItemClickListener;
    }

    public ObservableBoolean getIsErrorVisible(){
        return isErrorVisible;
    }

    public ObservableArrayList<Vacancies.ItemsBean> getVacancies(){
        return mVacancies;
    }

    public ObservableBoolean getIsLoading(){
        return isLoading;
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener(){
        return onRefreshListener;
    }
}
