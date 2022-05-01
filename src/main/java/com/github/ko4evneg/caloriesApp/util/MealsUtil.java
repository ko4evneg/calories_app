package com.github.ko4evneg.caloriesApp.util;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 10, 0), "Breakfast", 500),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 13, 0), "Dinner", 1000),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 20, 0), "Supper", 500),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 0, 0), "Edge case", 100),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 1000),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 500),
                new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 410)
        );

        List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(9, 0), LocalTime.of(14, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalories = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> mapFromMeal(meal, dailyCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    //TODO: add mapper
    private static MealTo mapFromMeal(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
