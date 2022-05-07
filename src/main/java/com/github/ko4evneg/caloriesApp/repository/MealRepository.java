package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealRepository {
    List<Meal> getAll();

    Optional<Meal> get(Integer id);

    Meal save(Meal meal);

    void delete(Integer id);
}
