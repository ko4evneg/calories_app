package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealInMemoryRepository;

import java.util.List;

public class MealService {
    private final MealInMemoryRepository mealInMemoryRepository = new MealInMemoryRepository();

    public List<Meal> getMeals() {
        return mealInMemoryRepository.getMeals();
    }
}
