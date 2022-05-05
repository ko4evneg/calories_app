package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealRepository {
    List<Meal> getAll();

    Optional<Meal> get(int id);

    void save(Meal meal);

    void delete(int id);
}
