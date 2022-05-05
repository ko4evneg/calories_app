package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealMemoryRepository;

import java.util.List;
import java.util.Optional;

public class MealService {
    private final MealMemoryRepository mealMemoryRepository = new MealMemoryRepository();

    public List<Meal> getAll() {
        return mealMemoryRepository.getAll();
    }

    public Optional<Meal> get(int mealId) {
        return mealMemoryRepository.get(mealId);
    }
}
