package com.example.headhunter.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.R;
import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.ui.vacancies.VacanciesActivity;
import com.example.headhunter.ui.vacancies.VacanciesAdapter;
import com.example.headhunter.ui.vacancies.VacanciesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomBindingAdapter{

    @BindingAdapter({"bind:data", "bind:clickHandler"})
    public static void configureRecyclerView(RecyclerView recyclerView, List<Vacancies.ItemsBean> vacancies,
                                             VacanciesAdapter.OnItemClickListener onItemClickListener){

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

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
                                                     ArrayList<String> items){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(autoCompleteTextView.getContext(),
                R.layout.support_simple_spinner_dropdown_item, items);
        autoCompleteTextView.setAdapter(adapter);
    }


}
