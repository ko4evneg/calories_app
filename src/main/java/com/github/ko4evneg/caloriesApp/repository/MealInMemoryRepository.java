package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealInMemoryRepository {
    public List<Meal> getMeals() {
        return Arrays.asList(
                new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 10, 0), "Breakfast", 500),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 13, 0), "Dinner", 1000),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 20, 0), "Supper", 500),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 0, 0), "Edge case", 100),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 1000),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 500),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 410)
        );
    }
}
