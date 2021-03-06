package com.example.headhunter.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.data.model.ExperienceResponse;
import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesAdapter;
import com.example.headhunter.ui.vacancies.VacanciesFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomBindingAdapter{

    @BindingAdapter({"bind:data", "bind:clickHandler"})
    public static void configureRecyclerView(RecyclerView recyclerView, List<Vacancies.ItemsBean> vacancies,
                                             VacanciesAdapter.OnItemClickListener onItemClickListener){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        VacanciesAdapter adapter = new VacanciesAdapter(vacancies, onItemClickListener);
        recyclerView.setAdapter(adapter);

    }

    @BindingAdapter({"bind:refreshState", "bind:onRefresh"})
    public static void configureSwipeRefreshLayout(SwipeRefreshLayout layout, boolean isLoading,
                                                   SwipeRefreshLayout.OnRefreshListener listener){
        layout.setOnRefreshListener(listener);
        layout.post(() -> layout.setRefreshing(isLoading));

    }

    @BindingAdapter("bind:autoCompleteData")
    public static void configureAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView,
                                                     MutableLiveData<List<String>> items){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(autoCompleteTextView.getContext(),
                R.layout.list_item, items.getValue());
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) autoCompleteTextView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }
        });
    }

    @BindingAdapter("bind:image")
    public static void picassoLogo(ImageView imageView, String url){
        Picasso.get().load(url).into(imageView);
    }

    @BindingAdapter("bind:experienceMap")
    public static void configureRadioGroup(RadioGroup radioGroup, Map<String, String> experienceMap){
        experienceMap.put("Показать все", null);
        List<String> experienceNames = new ArrayList<String>(experienceMap.keySet());
        for (String experienceName : experienceNames){
            RadioButton radioButton = new RadioButton(radioGroup.getContext());
            radioButton.setText(experienceName);
            radioButton.setId(Math.abs(experienceName.hashCode()));
            radioGroup.addView(radioButton);
        }
    }
}
