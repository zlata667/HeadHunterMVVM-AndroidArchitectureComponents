package com.example.headhunter.data.api;

import com.example.headhunter.data.model.Country;
import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.data.model.Vacancy;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HeadHunterApi{

    @GET("/vacancies")
    Single<Vacancies> getVacancies(@Query("text") String searchText, @Query("area") String searchArea,
                                   @Query("experience") String searchExperience);

    @GET("/vacancies/{vacancy_id}")
    Single<Vacancy> getVacancy(@Path("vacancy_id") String vacancyId);

    @GET("/areas")
    Observable<List<Country>> getCities();
}
