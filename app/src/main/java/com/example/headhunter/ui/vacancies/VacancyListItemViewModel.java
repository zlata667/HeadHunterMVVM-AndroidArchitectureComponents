package com.example.headhunter.ui.vacancies;

import android.text.Html;

import com.example.headhunter.data.model.Vacancies;

public class VacancyListItemViewModel{

    private String mId;
    private String mTitle;
    private String mDescription;
    private String mCity;
    private String mEmployer;

    public VacancyListItemViewModel(Vacancies.ItemsBean vacancy){
        mTitle = vacancy.getName();
        if (vacancy.getSnippet().getResponsibility() != null){
            mDescription = Html.fromHtml(vacancy.getSnippet().getResponsibility()).toString();
        }
        if (vacancy.getArea().getName() != null){
            mCity = vacancy.getArea().getName();
        }
        mId = vacancy.getId();
    }

    public String getId(){
        return mId;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getCity(){
        return mCity;
    }
}
