package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MealRepository {
    List<Meal> getAll(Integer userId);

    List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer userId);

    Optional<Meal> get(Integer id, Integer userId);

    Meal save(Meal meal, Integer userId);

    boolean delete(Integer id, Integer userId);
}
