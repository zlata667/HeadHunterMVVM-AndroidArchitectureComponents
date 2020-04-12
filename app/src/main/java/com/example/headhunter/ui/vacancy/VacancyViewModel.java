package com.example.headhunter.ui.vacancy;

import android.text.Html;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Vacancy;
import com.example.headhunter.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacancyViewModel{

    private Disposable disposable;
    private String mVacancyId;

    private ObservableField<String> vacancyName = new ObservableField<>();
    private ObservableField<String> employerName = new ObservableField<>();
    private ObservableField<String> salary = new ObservableField<>();
    private ObservableField<String> vacancyDescription = new ObservableField<>();

    private ObservableBoolean isErrorVisible = new ObservableBoolean(false);
    private ObservableBoolean isLoading = new ObservableBoolean(false);
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> loadVacancy(mVacancyId);

    VacancyViewModel(String vacancyId){
        mVacancyId = vacancyId;
    }

    void loadVacancy(String vacancyId){
        disposable = ApiUtils.getApiService().getVacancy(vacancyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doFinally(() -> isLoading.set(false))
                .subscribe(vacancy -> {
                            isErrorVisible.set(false);
                            bind(vacancy);
                        },
                        throwable -> isErrorVisible.set(true));
    }

    public void bind(Vacancy vacancy){
        vacancyName.set(vacancy.getName());
        employerName.set(vacancy.getEmployer().getName());
        salary.set(String.valueOf(vacancy.getSalary().getFrom())
                .concat(" ")
                .concat(vacancy.getSalary().getCurrency()));
        vacancyDescription.set(Html.fromHtml(vacancy.getDescription()).toString());
    }

    void dispatchDetach(){
        if (disposable != null){
            disposable.dispose();
        }
    }

    public ObservableField<String> getVacancyName(){
        return vacancyName;
    }

    public ObservableField<String> getEmployerName(){
        return employerName;
    }

    public ObservableField<String> getSalary(){
        return salary;
    }

    public ObservableField<String> getVacancyDescription(){
        return vacancyDescription;
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
}
