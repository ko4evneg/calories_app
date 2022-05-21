package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    List<Meal> getAll(Integer userId);

    Meal get(Integer mealId, Integer userId);

    Meal save(Meal meal, Integer userId);

    void delete(Integer mealId, Integer userId);

    List<Meal> getBetweenInclusive(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
}
