package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.util.List;

public interface MealService {
    List<Meal> getAll();

    Meal get(Integer mealId);

    Meal save(Meal meal);

    void delete(Integer mealId);
}
