package com.example.headhunter.ui.vacancies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headhunter.R;
import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.databinding.VacancyBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VacanciesAdapter extends RecyclerView.Adapter<VacanciesHolder>{

    private final OnItemClickListener mOnItemClickListener;
    @NonNull
    private final List<Vacancies.ItemsBean> vacancyList;


    public VacanciesAdapter(List<Vacancies.ItemsBean> vacancies, OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
        vacancyList = vacancies;
    }

    @NonNull
    @Override
    public VacanciesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VacancyBinding binding = VacancyBinding.inflate(inflater, parent, false);
        return new VacanciesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VacanciesHolder holder, int position){
        holder.bind(vacancyList.get(position), mOnItemClickListener);
    }

    @Override
    public int getItemCount(){
        return vacancyList.size();
    }
//
//    void setItems(Collection<Vacancies.ItemsBean> vacancies){
//       vacancyList.addAll(vacancies);
//       notifyDataSetChanged();
//    }

   public void clearItems(){
       vacancyList.clear();
       notifyDataSetChanged();
   }

    public interface OnItemClickListener {
        void onItemClick(String vacancyId);
    }
}
