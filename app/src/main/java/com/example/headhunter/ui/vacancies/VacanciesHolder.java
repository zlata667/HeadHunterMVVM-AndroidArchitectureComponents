package com.example.headhunter.ui.vacancies;

import androidx.recyclerview.widget.RecyclerView;

import com.example.headhunter.data.model.Vacancies;
import com.example.headhunter.databinding.VacancyBinding;

class VacanciesHolder extends RecyclerView.ViewHolder{

    private VacancyBinding mVacancyBinding;

    VacanciesHolder(VacancyBinding binding){
        super(binding.getRoot());
        mVacancyBinding = binding;
    }

    void bind(Vacancies.ItemsBean vacancy, VacanciesAdapter.OnItemClickListener onItemClickListener){
        mVacancyBinding.setVacancy(new VacancyListItemViewModel(vacancy));
        mVacancyBinding.setOnItemClickListener(onItemClickListener);
        mVacancyBinding.executePendingBindings();
    }
}
