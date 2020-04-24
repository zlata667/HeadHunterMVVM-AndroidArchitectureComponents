package com.example.headhunter.ui.vacancies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.headhunter.R;

public class FilterDialogViewModel extends ViewModel{

    private MutableLiveData<String> mSearchText = new MutableLiveData<>();
    private MutableLiveData<String> mExperienceId = new MutableLiveData<>();
    private MutableLiveData<String> mRegionName = new MutableLiveData<>();
    private MutableLiveData<Integer> checkedId = new MutableLiveData<>();


    public FilterDialogViewModel(String searchText, String regionName, String experienceId){
        mSearchText.setValue(searchText);
        mRegionName.setValue(regionName);
        if (experienceId == null){
            checkedId.setValue(R.id.rb_exp_all);
        } else{
            switch (experienceId) {
                case "noExperience":
                    checkedId.postValue(R.id.rb_exp_0);
                    break;
                case "between1And3":
                    checkedId.postValue(R.id.rb_exp_1_3);
                    break;
                case "between3And6":
                    checkedId.postValue(R.id.rb_exp_3_6);
                    break;
                case "moreThan6":
                    checkedId.postValue(R.id.rb_exp_6);
                    break;

            }
        }

    }

    public void onExperienceChanged(RadioGroup radioGroup, int id){
        switch (id) {
            case R.id.rb_exp_0:
                mExperienceId.setValue("noExperience");
                break;
            case R.id.rb_exp_1_3:
                mExperienceId.setValue("between1And3");
                break;
            case R.id.rb_exp_3_6:
                mExperienceId.setValue("between3And6");
                break;
            case R.id.rb_exp_6:
                mExperienceId.setValue("moreThan6");
                break;
            case R.id.rb_exp_all:
                mExperienceId.setValue(null);
                break;
        }
    }

    public void onButtonClick(Context context){
        Intent intent = new Intent(context, VacanciesActivity.class);
        Bundle args = new Bundle();
        args.putString(VacanciesFragment.SEARCH_TEXT, mSearchText.getValue());
        args.putString(VacanciesFragment.SEARCH_EXPERIENCE_ID, mExperienceId.getValue());
        //args.putString(VacanciesFragment.SEARCH_REGION_NAME, mRegionName.getValue()); нужно передавать id
        intent.putExtra(VacanciesActivity.SEARCH_KEY, args);
        context.startActivity(intent);

    }

    public MutableLiveData<String> getSearchText(){
        return mSearchText;
    }

    public void setSearchText(MutableLiveData<String> SearchText){
        mSearchText = SearchText;
    }

    public MutableLiveData<String> getRegionName(){
        return mRegionName;
    }

    public MutableLiveData<Integer> getCheckedId(){
        return checkedId;
    }
}
