package com.example.headhunter.utils;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.ui.vacancies.VacanciesAdapter;

import java.util.List;

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
}
