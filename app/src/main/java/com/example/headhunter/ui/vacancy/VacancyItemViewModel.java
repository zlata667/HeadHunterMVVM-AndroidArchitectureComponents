package com.example.headhunter.ui.vacancy;

import android.text.Html;

import com.example.headhunter.data.model.Vacancy;

public class VacancyItemViewModel{

    private String vacancyName;
    private String employerName;
    private String salary;
    private String vacancyDescription;

    public VacancyItemViewModel(Vacancy vacancy){
        vacancyName = vacancy.getName();
        employerName = vacancy.getEmployer().getName();
        if (vacancy.getSalary() != null){
            salary = String.valueOf(vacancy.getSalary().getFrom())
                    .concat(" ")
                    .concat(vacancy.getSalary().getCurrency());
        }
        vacancyDescription = Html.fromHtml(vacancy.getDescription()).toString();
    }

    public String getVacancyName(){
        return vacancyName;
    }

    public String getEmployerName(){
        return employerName;
    }

    public String getSalary(){
        return salary;
    }

    public String getVacancyDescription(){
        return vacancyDescription;
    }
}
