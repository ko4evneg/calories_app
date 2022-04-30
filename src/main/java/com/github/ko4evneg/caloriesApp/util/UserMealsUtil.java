package com.github.ko4evneg.caloriesApp.util;

import com.github.ko4evneg.caloriesApp.model.UserMeal;
import com.github.ko4evneg.caloriesApp.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 29, 10, 0), "Breakfast", 500),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 29, 13, 0), "Dinner", 1000),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 29, 20, 0), "Supper", 500),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 30, 0, 0), "Edge case", 100),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 1000),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 500),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByStreams(meals, LocalTime.of(9, 0), LocalTime.of(14, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();
        meals.forEach(meal -> dailyCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> mapFromMeal(meal, dailyCalories.get(meal.getDateTime().toLocalDate()) > 2000))
                .collect(Collectors.toList());
    }

    private static UserMealWithExcess mapFromMeal(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
