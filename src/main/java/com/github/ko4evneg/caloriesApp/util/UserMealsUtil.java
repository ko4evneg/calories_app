package com.github.ko4evneg.caloriesApp.util;

import com.github.ko4evneg.caloriesApp.model.UserMeal;
import com.github.ko4evneg.caloriesApp.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 500),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 1000),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 500),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 31, 0, 0), "Edge case", 100),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 31, 10, 0), "Breakfast", 1000),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 31, 13, 0), "Dinner", 500),
                new UserMeal(LocalDateTime.of(2022, Month.APRIL, 31, 20, 0), "Supper", 410)
                );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        return null;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
