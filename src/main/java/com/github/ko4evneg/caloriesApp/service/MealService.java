package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealService {
    List<Meal> getAll();

    Optional<Meal> get(Integer mealId);

    void save(Meal meal);

    void delete(Integer mealId);
}
