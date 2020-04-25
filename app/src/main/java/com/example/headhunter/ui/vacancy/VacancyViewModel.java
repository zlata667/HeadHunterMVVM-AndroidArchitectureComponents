package com.example.headhunter.ui.vacancy;

import android.text.Html;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Vacancy;
import com.example.headhunter.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VacancyViewModel extends ViewModel{

    private Disposable disposable;
    private String mVacancyId;

    private MutableLiveData<String> vacancyName = new MutableLiveData<>();
    private MutableLiveData<String> employerName = new MutableLiveData<>();
    private MutableLiveData<String> salary = new MutableLiveData<>();
    private MutableLiveData<String> vacancyDescription = new MutableLiveData<>();
    private MutableLiveData<String> vacancyLogo = new MutableLiveData<>();

    private MutableLiveData<Integer> isErrorVisible = new MutableLiveData();
    private MutableLiveData<Integer> isVacancyVisible = new MutableLiveData();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData();
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> loadVacancy(mVacancyId);

    public VacancyViewModel(String vacancyId){
        mVacancyId = vacancyId;
        loadVacancy(vacancyId);
    }

    void loadVacancy(String vacancyId){
        disposable = ApiUtils.getApiService().getVacancy(vacancyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.postValue(true))
                .doFinally(() -> isLoading.postValue(false))
                .subscribe(vacancy -> {
                            isErrorVisible.postValue(View.GONE);
                            isVacancyVisible.postValue(View.VISIBLE);
                            bind(vacancy);
                        },
                        throwable ->{
                            isErrorVisible.postValue(View.VISIBLE);
                            isVacancyVisible.postValue(View.GONE);
                        });
    }

    public void bind(Vacancy vacancy){
        vacancyName.postValue(vacancy.getName());
        employerName.postValue(vacancy.getEmployer().getName());
        if (vacancy.getSalary() != null){
            salary.postValue(String.valueOf(vacancy.getSalary().getFrom())
                    .concat(" ")
                    .concat(vacancy.getSalary().getCurrency()));
        }
        vacancyDescription.postValue(Html.fromHtml(vacancy.getDescription()).toString());
        if (vacancy.getEmployer().getLogo_urls() != null) {
            vacancyLogo.postValue(vacancy.getEmployer().getLogo_urls().getOriginal());
        }
    }

    @Override
    public void onCleared(){
        if (disposable != null){
            disposable.dispose();
        }
    }

    public MutableLiveData<String> getVacancyName(){
        return vacancyName;
    }

    public MutableLiveData<String> getEmployerName(){
        return employerName;
    }

    public MutableLiveData<String> getSalary(){
        return salary;
    }

    public MutableLiveData<String> getVacancyDescription(){
        return vacancyDescription;
    }

    public MutableLiveData<Integer> getIsErrorVisible(){
        return isErrorVisible;
    }

    public MutableLiveData<Integer> getIsVacancyVisible(){
        return isVacancyVisible;
    }

    public MutableLiveData<Boolean> getIsLoading(){
        return isLoading;
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener(){
        return onRefreshListener;
    }

    public MutableLiveData<String> getVacancyLogo(){
        return vacancyLogo;
    }
}
